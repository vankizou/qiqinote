package com.qiqinote.controller

import com.qiqinote.constant.CodeEnum
import com.qiqinote.constant.DBConst
import com.qiqinote.constant.WebConst
import com.qiqinote.model.Page
import com.qiqinote.po.Picture
import com.qiqinote.service.PictureService
import com.qiqinote.util.*
import com.qiqinote.vo.ResultVO
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream
import java.util.*
import javax.imageio.ImageIO
import kotlin.collections.ArrayList

/**
 * Created by vanki on 2018/2/24 15:44.
 */
@RestController
@RequestMapping("/image")
class ImageController @Autowired constructor(
        private val pictureService: PictureService,
        private val env: Environment
) : BaseController() {
    private val uploadMaxNum = 20
    private val imageAllowTypeList = arrayListOf<String>()

    private val imageAllowType = env["qiqinote.image.allow.type"]
    private val imageBasePath = env["qiqinote.image.basepath"]
    private val imageDomain = env["qiqinote.image.domain"]

    @PostMapping("/uploadMulti" + WebConst.needLoginJsonSuffix)
    fun uploadMulti(@RequestParam images: Array<MultipartFile>?, useType: Int?): ResultVO<MutableList<Picture>> {
        val uploadNum = images?.size
        if (uploadNum == null || uploadNum == 0) return ResultVO(CodeEnum.IMAGE_NOT_FOUNT)
        if (uploadNum > uploadMaxNum) return ResultVO(CodeEnum.IMAGE_UPLOAD_TOO_MANY)
        var useTypeTmp = useType
        if (useTypeTmp == null) useTypeTmp = DBConst.Picture.useTypeNote

        val baseDir = File(imageBasePath)
        if (!baseDir.exists()) {
            baseDir.mkdirs()
            FileUtil.setFilePermission(imageBasePath, 755)
        }

        val picList = ArrayList<Picture>(uploadNum)
        val userId = this.getLoginUserId()

        try {
            for (image in images) {
                log.info(image.originalFilename)
                val imageType = ImageTypeUtil.getFileType(image.inputStream)
                if (StringUtil.isEmpty(imageType) || !isAllowType(imageType)) continue

                val uuid = UUIDUtil.getUUID()
                var imageRelationPath = getSubDir()
                val imageParentDir = File(imageBasePath, imageRelationPath)

                if (!imageParentDir.exists()) { // 生成父文件夹
                    imageParentDir.mkdirs()
                    FileUtil.setFilePermission(imageParentDir.getAbsolutePath(), 755)
                }
                imageRelationPath = imageRelationPath + File.separatorChar + uuid + "." + imageType

                val imageFile = File(baseDir, imageRelationPath)

                var bi = ImageIO.read(image.inputStream)
                var width = bi.width
                var height = bi.height

                FileUtils.copyInputStreamToFile(image.inputStream, imageFile)
                image.inputStream.close()
                FileUtil.setFilePermission(imageFile, 644)

                val isResize = resizeImg(useTypeTmp, width, imageFile)    // 图片缩放
                if (isResize) {
                    bi = ImageIO.read(imageFile)
                    width = bi.width
                    height = bi.height
                }
                imageRelationPath = imageRelationPath.replace("\\", "/")

                val pic = Picture()
                pic.uuid = uuid
                pic.width = width
                pic.height = height
                pic.path = imageRelationPath
                pic.size = imageFile.length()
                pic.type = imageType
                pic.useType = useTypeTmp
                image.originalFilename?.let { pic.name = it.substring(0, it.lastIndexOf(".")) }
                pic.userId = userId

                val result = this.pictureService.add(pic)
                if (result > 0) {
                    log.info("上传图片，路径：$imageRelationPath")
                    pic.path = imageDomain + pic.path
                    picList.add(pic)
                }
            }
            return ResultVO(picList)
        } catch (e: Exception) {
            throw e
        } finally {
            for (image in images) {
                image.inputStream.close()
            }
        }
    }

    @GetMapping("/page" + WebConst.needLoginJsonSuffix)
    fun page(useType: Int?, currPage: Int?, pageSize: Int?, navNum: Int?): ResultVO<Page<Picture>> {
        val useTypeTmp = useType ?: DBConst.Picture.useTypeNote
        val currPageTmp = currPage ?: Page.firstPage
        val pageSizeTmp = pageSize ?: 10
        val navNumTmp = navNum ?: 10
        return ResultVO(this.pictureService.page(this.getLoginUserId(), useTypeTmp, currPageTmp, pageSizeTmp, navNumTmp))
    }

    private fun isAllowType(type: String?): Boolean {
        if (type == null) return false;

        if (imageAllowTypeList.isEmpty()) {
            imageAllowTypeList.addAll(imageAllowType.split(","))
        }
        return imageAllowTypeList.contains(type)
    }

    companion object {
        private val log = Logger.getLogger(ImageController::class.java)

        /**
         * 图片相对路径父文件夹
         */
        fun getSubDir(): String {
            val calendar = Calendar.getInstance()

            val subDir = StringBuilder()
            subDir.append(calendar.get(Calendar.YEAR))
            subDir.append(File.separatorChar)
            subDir.append(calendar.get(Calendar.MONTH) + 1)
            subDir.append(File.separatorChar)
            subDir.append(calendar.get(Calendar.DAY_OF_MONTH))

            return subDir.toString()
        }

        private fun resizeImg(useType: Int?, width: Int, imageFile: File): Boolean {
            val maxWidth: Int

            when (useType) {
                DBConst.Picture.useTypeNote -> maxWidth = 800
                DBConst.Picture.useTypeAvatar -> maxWidth = 300
                else -> maxWidth = 500
            }

            if (width <= maxWidth) return false

            /**
             * TODO: 缩放图片，可以把这个操作加到消息队列里去
             */
            val absPath: String = imageFile.absolutePath
            try {
                ImageMagickUtil.resizeByWidth(maxWidth, absPath, absPath)
                log.info("缩放图片：${imageFile.absolutePath}")
                return true
            } catch (e: Exception) {
                log.error("缩放图片失败：$absPath", e)
                return false
            }

        }
    }
}