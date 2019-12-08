package com.qiqinote.controller

import com.qiqinote.anno.NeedLogin
import com.qiqinote.constant.CodeEnum
import com.qiqinote.constant.DBConst
import com.qiqinote.dto.PictureDTO
import com.qiqinote.po.Picture
import com.qiqinote.service.PictureService
import com.qiqinote.util.*
import com.qiqinote.vo.ResultVO
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import org.apache.commons.io.FileUtils
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.collections.ArrayList

/**
 * Created by vanki on 2018/2/24 15:44.
 */
@Api("图片相关")
@RestController
@RequestMapping("/pic")
class PictureController @Autowired constructor(
        private val pictureService: PictureService,
        environment: Environment
) : BaseController() {
    private val uploadMaxNum = 20
    private val imageAllowTypeList = arrayListOf<String>()

    private val imageAllowType = environment["qiqinote.image.allow.type"]!!
    private val imageBasePath = environment["qiqinote.image.basepath"]!!
    private val imageSizeNote = environment["qiqinote.image.size.note"]!!.toInt()
    private val imageSizeAvatar = environment["qiqinote.image.size.avatar"]!!.toInt()
    private val imageSizeOther = environment["qiqinote.image.size.other"]!!.toInt()

    @NeedLogin
    @ApiOperation("上传图片（限制图片类型为：jpg,jpeg,png,gif）")
    @ApiImplicitParams(
            ApiImplicitParam(name = "images", value = "图片二进制流（数组）"),
            ApiImplicitParam(name = "useType", value = "图片类型；1笔记，2头像。默认1")
    )
    @PostMapping("/uploadMulti")
    fun uploadMulti(@RequestParam images: Array<MultipartFile>?, useType: Int?): ResultVO<List<PictureDTO>> {
        val uploadNum = images?.size
        if (uploadNum == null || uploadNum == 0) {
            return ResultVO(CodeEnum.IMAGE_NOT_FOUNT)
        }
        if (uploadNum > uploadMaxNum) {
            return ResultVO(CodeEnum.IMAGE_UPLOAD_TOO_MANY)
        }
        val useTypeTmp = useType ?: DBConst.Picture.useTypeNote

        val baseDir = File(imageBasePath)
        if (!baseDir.exists()) {
            baseDir.mkdirs()
            FileUtil.setFilePermission(imageBasePath, 755)
        }

        val picList = ArrayList<PictureDTO>(uploadNum)
        val userId = this.getLoginUserId()

        try {
            for (image in images) {
                log.info(image.originalFilename)
                val imageType = ImageTypeUtil.getFileType(image.inputStream)
                if (StringUtil.isEmpty(imageType) || !isAllowType(imageType)) {
                    continue
                }

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

                val isResize = tryResizeImg(useTypeTmp, width, imageFile, imageSizeNote, imageSizeAvatar, imageSizeOther)    // 图片缩放
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
                    pic.id = result
                    pic.path = imageDomain + pic.path
                    picList.add(PictureDTO(this.imageDomain, pic))
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

    @NeedLogin
    @ApiOperation("获取图片列表")
    @ApiImplicitParams(
            ApiImplicitParam(name = "useType", value = "图片类型；1笔记，2头像，默认1"),
            ApiImplicitParam(name = "page", value = "第几页，从1开始，默认1"),
            ApiImplicitParam(name = "row", value = "每页获取最大数据量，默认10")
    )
    @GetMapping("/list")
    fun page(useType: Int?, page: Int?, row: Int?): ResultVO<List<PictureDTO>> {
        val useTypeTmp = useType ?: DBConst.Picture.useTypeNote
        val currPageTmp = 1
        val pageSizeTmp = row ?: 10

        val list = this.pictureService.list(this.getLoginUserId(), useTypeTmp, currPageTmp, pageSizeTmp)
        val pics = mutableListOf<PictureDTO>()
        list.forEach {
            pics.add(PictureDTO(this.imageDomain, it))
        }
        return ResultVO(pics)
    }

    private fun isAllowType(type: String?): Boolean {
        if (type == null) {
            return false
        }

        if (imageAllowTypeList.isEmpty()) {
            imageAllowTypeList.addAll(imageAllowType.split(","))
        }
        return imageAllowTypeList.contains(type)
    }

    companion object {
        private val log = Logger.getLogger(PictureController::class.java)

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

        private fun tryResizeImg(useType: Int?, width: Int, imageFile: File, noteSize: Int, avatarSize: Int, otherSize: Int): Boolean {
            val maxWidth = when (useType) {
                DBConst.Picture.useTypeNote -> noteSize
                DBConst.Picture.useTypeAvatar -> avatarSize
                else -> otherSize
            }

            if (width <= maxWidth) {
                return false
            }

            /**
             * TODO: 缩放图片，可以把这个操作加到消息队列里去
             */
            val absPath: String = imageFile.absolutePath
            return try {
                ImageMagickUtil.resizeByWidth(maxWidth, absPath, absPath)
                log.info("缩放图片：$absPath")
                true
            } catch (e: Exception) {
                log.error("缩放图片失败：$absPath", e)
                false
            }
        }
    }
}