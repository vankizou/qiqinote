package com.qiqinote.model

import java.util.*

/**
 * Created by vanki on 2018/3/8 20:29.
 */
class Page<T> {
    /**
     * 数据库总行数
     */
    var totalRow: Int = 0

    /**
     * 查询起始行
     */
    var startRow: Int = 0

    /**
     * 一页多少行
     */
    var pageSize = 0

    /**
     * 一共多少页
     */
    var totalPage: Int = 0

    /**
     * 第几页
     */
    var currPage: Int = 0

    /**
     * 上一页
     */
    var prevPage: Int = 0

    /**
     * 下一页
     */
    var nextPage: Int = 0

    /**
     * 首页
     */
    var beginPage = 0

    /**
     * 尾页
     */
    var endPage: Int = 0

    /**
     * 一共显示多少导航
     */
    var navSize = 0

    /**
     * 显示导航起始
     */
    var beginNav: Int = 0

    /**
     * 显示的导航结束
     */
    var endNav: Int = 0

    /**
     * 分页查询之后的数据
     */
    var data: MutableList<T> = ArrayList(0)

    constructor()

    constructor(currPage: Int, pageSize: Int, totalRow: Int) : this(currPage, pageSize, totalRow, 10)

    constructor(currPage: Int, pageSize: Int, totalRow: Int, navSize: Int) {
        this.currPage = currPage
        this.pageSize = pageSize
        this.totalRow = totalRow
        this.navSize = navSize
        this.currPage = if (this.currPage < 1) firstPage else this.currPage
        this.pageSize = if (this.pageSize < 0) 10 else this.pageSize
        this.totalRow = if (this.totalRow < 0) 0 else this.totalRow
        this.navSize = if (this.navSize < 0) 10 else this.navSize
        this.totalPage = Math.ceil(this.totalRow * 1.0 / this.pageSize).toInt()
        this.endPage = this.totalPage
        this.currPage = Math.min(this.endPage, this.currPage)
        this.currPage = Math.max(this.beginPage, this.currPage)

        this.startRow = Math.max(0, (this.currPage - 1) * this.pageSize)

        this.nextPage = Math.min(this.endPage, this.currPage + 1)
        this.prevPage = Math.max(this.beginPage, this.currPage - 1)

        this.beginNav = Math.max(this.beginPage, this.currPage - this.navSize / 2)
        this.endNav = Math.min(this.endPage, this.beginNav + this.navSize - 1)

        if (this.endNav - this.beginNav < this.navSize - 1) {
            this.beginNav = Math.max(this.beginPage, this.endPage - this.navSize + 1)
        }
    }

    companion object {
        const val firstPage = 1

        fun getStartRow(currPage: Int, pageSize: Int) = if (currPage <= 1 || pageSize <= 0) 0 else (currPage - 1) * pageSize
    }
}