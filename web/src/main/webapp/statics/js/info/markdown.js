/**
 * Created by vanki on 2017/5/21.
 */

var markdownExample;
$(function () {
    markdownExample = "### 主要特性\r\n" +
        "\r\n" +
        "- 支持“标准”Markdown / CommonMark和Github风格的语法，也可变身为代码编辑器；\r\n" +
        "- 支持实时预览、图片（跨域）上传、预格式文本/代码/表格插入、代码折叠、搜索替换、只读模式、自定义样式主题和多语言语法高亮等功能；\r\n" +
        "- 支持ToC（Table of Contents）、Emoji表情、Task lists、@链接等Markdown扩展语法；\r\n" +
        "- 支持TeX科学公式（基于KaTeX）、流程图 Flowchart 和 时序图 Sequence Diagram;\r\n" +
        "- 支持识别和解析HTML标签，并且支持自定义过滤标签解析，具有可靠的安全性和几乎无限的扩展性；\r\n" +
        "- 支持 AMD / CMD 模块化加载（支持 Require.js & Sea.js），并且支持自定义扩展插件；\r\n" +
        "- 兼容主流的浏览器（IE8+）和Zepto.js，且支持iPad等平板设备；\r\n" +
        "- 支持自定义主题样式；\r\n" +
        "\r\n" +
        "**目录 (Table of Contents)**\r\n" +
        "\r\n" +
        "[TOCM]\r\n" +
        "\r\n" +
        "[TOC]\r\n" +
        "\r\n" +
        "# Heading 1\r\n" +
        "## Heading 2\r\n" +
        "### Heading 3\r\n" +
        "#### Heading 4\r\n" +
        "##### Heading 5\r\n" +
        "###### Heading 6\r\n" +
        "# Heading 1 link [Heading link](https://github.com/pandao/editor.md \"Heading link\")\r\n" +
        "## Heading 2 link [Heading link](https://github.com/pandao/editor.md \"Heading link\")\r\n" +
        "### Heading 3 link [Heading link](https://github.com/pandao/editor.md \"Heading link\")\r\n" +
        "#### Heading 4 link [Heading link](https://github.com/pandao/editor.md \"Heading link\") Heading link [Heading link](https://github.com/pandao/editor.md \"Heading link\")\r\n" +
        "##### Heading 5 link [Heading link](https://github.com/pandao/editor.md \"Heading link\")\r\n" +
        "###### Heading 6 link [Heading link](https://github.com/pandao/editor.md \"Heading link\")\r\n" +
        "\r\n" +
        "#### 标题（用底线的形式）Heading (underline)\r\n" +
        "\r\n" +
        "This is an H1\r\n" +
        "=============\r\n" +
        "\r\n" +
        "This is an H2\r\n" +
        "-------------\r\n" +
        "\r\n" +
        "### 字符效果和横线等\r\n" +
        "\r\n" +
        "----\r\n" +
        "\r\n" +
        "~~删除线~~ <s>删除线（开启识别HTML标签时）</s>\r\n" +
        "*斜体字*      _斜体字_\r\n" +
        "**粗体**  __粗体__\r\n" +
        "***粗斜体*** ___粗斜体___\r\n" +
        "\r\n" +
        "上标：X<sub>2</sub>，下标：O<sup>2</sup>\r\n" +
        "\r\n" +
        "**缩写(同HTML的abbr标签)**\r\n" +
        "\r\n" +
        "> 即更长的单词或短语的缩写形式，前提是开启识别HTML标签时，已默认开启\r\n" +
        "\r\n" +
        "The <abbr title=\"Hyper Text Markup Language\">HTML</abbr> specification is maintained by the <abbr\r\n" +
        "                title=\"World Wide Web Consortium\">W3C</abbr>.\r\n" +
        "\r\n" +
        "### 引用 Blockquotes\r\n" +
        "\r\n" +
        "> 引用文本 Blockquotes\r\n" +
        "\r\n" +
        "引用的行内混合 Blockquotes\r\n" +
        "\r\n" +
        "> 引用：如果想要插入空白换行`即<br/>标签`，在插入处先键入两个以上的空格然后回车即可，[普通链接](http://localhost/)。\r\n" +
        "\r\n" +
        "### 锚点与链接 Links\r\n" +
        "\r\n" +
        "[普通链接](http://localhost/)\r\n" +
        "\r\n" +
        "[普通链接带标题](http://localhost/ \"普通链接带标题\")\r\n" +
        "\r\n" +
        "直接链接：<https://github.com>\r\n" +
        "\r\n" +
        "[锚点链接][anchor-id]\r\n" +
        "\r\n" +
        "[anchor-id]: http://www.this-anchor-link.com/\r\n" +
        "\r\n" +
        "GFM a-tail link @pandao\r\n" +
        "\r\n" +
        "> @pandao\r\n" +
        "\r\n" +
        "### 多语言代码高亮 Codes\r\n" +
        "\r\n" +
        "#### 行内代码 Inline code\r\n" +
        "\r\n" +
        "执行命令：`npm install marked`\r\n" +
        "\r\n" +
        "#### 缩进风格\r\n" +
        "\r\n" +
        "即缩进四个空格，也做为实现类似`<pre>`预格式化文本(Preformatted Text)的功能。\r\n" +
        "\r\n" +
        "            <?php\r\n" +
        "        echo \"Hello world!\";\r\n" +
        "    ?>\r\n" +
        "\r\n" +
        "            预格式化文本：\r\n" +
        "\r\n" +
        "    | First Header  | Second Header |\r\n" +
        "    | ------------- | ------------- |\r\n" +
        "    | Content Cell  | Content Cell  |\r\n" +
        "    | Content Cell  | Content Cell  |\r\n" +
        "\r\n" +
        "#### JS代码　\r\n" +
        "\r\n" +
        "```javascript\r\n" +
        "function test(){\r\n" +
        "	console.log(\"Hello world!\");\r\n" +
        "}\r\n" +
        "\r\n" +
        "(function(){\r\n" +
        "    var box = function(){\r\n" +
        "        return box.fn.init();\r\n" +
        "    };\r\n" +
        "\r\n" +
        "    box.prototype = box.fn = {\r\n" +
        "        init : function(){\r\n" +
        "            console.log('box.init()');\r\n" +
        "\r\n" +
        "			return this;\r\n" +
        "        },\r\n" +
        "\r\n" +
        "		add : function(str){\r\n" +
        "			alert(\"add\", str);\r\n" +
        "\r\n" +
        "			return this;\r\n" +
        "		},\r\n" +
        "\r\n" +
        "		remove : function(str){\r\n" +
        "			alert(\"remove\", str);\r\n" +
        "\r\n" +
        "			return this;\r\n" +
        "		}\r\n" +
        "    };\r\n" +
        "\r\n" +
        "    box.fn.init.prototype = box.fn;\r\n" +
        "\r\n" +
        "    window.box =box;\r\n" +
        "})();\r\n" +
        "\r\n" +
        "var testBox = box();\r\n" +
        "testBox.add(\"jQuery\").remove(\"jQuery\");\r\n" +
        "```\r\n" +
        "\r\n" +
        "#### HTML代码 HTML codes\r\n" +
        "\r\n" +
        "```html\r\n" +
        "<!DOCTYPE html>\r\n" +
        "<html>\r\n" +
        "    <head>\r\n" +
        "        <mate charest=\"utf-8\"/>\r\n" +
        "        <title>Hello world!</title>\r\n" +
        "    </head>\r\n" +
        "    <body>\r\n" +
        "        <h1>Hello world!</h1>\r\n" +
        "    </body>\r\n" +
        "</html>\r\n" +
        "```\r\n" +
        "\r\n" +
        "### 图片 Images\r\n" +
        "\r\n" +
        "Image:\r\n" +
        "\r\n" +
        "![](https://pandao.github.io/editor.md/examples/images/4.jpg)\r\n" +
        "\r\n" +
        "> Follow your heart.\r\n" +
        "\r\n" +
        "![](https://pandao.github.io/editor.md/examples/images/8.jpg)\r\n" +
        "\r\n" +
        "> 图为：厦门白城沙滩\r\n" +
        "\r\n" +
        "图片加链接 (Image + Link)：\r\n" +
        "\r\n" +
        "[![](https://pandao.github.io/editor.md/examples/images/7.jpg)](https://pandao.github.io/editor.md/examples/images/7.jpg \"李健首张专辑《似水流年》封面\")\r\n" +
        "\r\n" +
        "> 图为：李健首张专辑《似水流年》封面\r\n" +
        "\r\n" +
        "----\r\n" +
        "\r\n" +
        "### 列表 Lists\r\n" +
        "\r\n" +
        "#### 无序列表（减号）Unordered Lists (-)\r\n" +
        "\r\n" +
        "- 列表一\r\n" +
        "- 列表二\r\n" +
        "- 列表三\r\n" +
        "\r\n" +
        "#### 无序列表（星号）Unordered Lists (*)\r\n" +
        "\r\n" +
        "* 列表一\r\n" +
        "* 列表二\r\n" +
        "* 列表三\r\n" +
        "\r\n" +
        "#### 无序列表（加号和嵌套）Unordered Lists (+)\r\n" +
        "\r\n" +
        "+ 列表一\r\n" +
        "+ 列表二\r\n" +
        "    + 列表二-1\r\n" +
        "    + 列表二-2\r\n" +
        "    + 列表二-3\r\n" +
        "+ 列表三\r\n" +
        "    * 列表一\r\n" +
        "    * 列表二\r\n" +
        "    * 列表三\r\n" +
        "\r\n" +
        "#### 有序列表 Ordered Lists (-)\r\n" +
        "\r\n" +
        "1. 第一行\r\n" +
        "2. 第二行\r\n" +
        "3. 第三行\r\n" +
        "\r\n" +
        "#### GFM task list\r\n" +
        "\r\n" +
        "- [x] GFM task list 1\r\n" +
        "- [x] GFM task list 2\r\n" +
        "- [ ] GFM task list 3\r\n" +
        "    - [ ] GFM task list 3-1\r\n" +
        "    - [ ] GFM task list 3-2\r\n" +
        "    - [ ] GFM task list 3-3\r\n" +
        "- [ ] GFM task list 4\r\n" +
        "    - [ ] GFM task list 4-1\r\n" +
        "    - [ ] GFM task list 4-2\r\n" +
        "\r\n" +
        "----\r\n" +
        "\r\n" +
        "### 绘制表格 Tables\r\n" +
        "\r\n" +
        "| 项目        | 价格   |  数量  |\r\n" +
        "| --------   | -----:  | :----:  |\r\n" +
        "| 计算机      | $1600   |   5     |\r\n" +
        "| 手机        |   $12   |   12   |\r\n" +
        "| 管线        |    $1    |  234  |\r\n" +
        "\r\n" +
        "First Header  | Second Header\r\n" +
        "------------- | -------------\r\n" +
        "Content Cell  | Content Cell\r\n" +
        "Content Cell  | Content Cell\r\n" +
        "\r\n" +
        "| First Header  | Second Header |\r\n" +
        "| ------------- | ------------- |\r\n" +
        "| Content Cell  | Content Cell  |\r\n" +
        "| Content Cell  | Content Cell  |\r\n" +
        "\r\n" +
        "| Function name | Description                    |\r\n" +
        "| ------------- | ------------------------------ |\r\n" +
        "| `help()`      | Display the help window.       |\r\n" +
        "| `destroy()`   | **Destroy your computer!**     |\r\n" +
        "\r\n" +
        "| Left-Aligned  | Center Aligned  | Right Aligned |\r\n" +
        "| :------------ |:---------------:| -----:|\r\n" +
        "| col 3 is      | some wordy text | $1600 |\r\n" +
        "| col 2 is      | centered        |   $12 |\r\n" +
        "| zebra stripes | are neat        |    $1 |\r\n" +
        "\r\n" +
        "| Item      | Value |\r\n" +
        "| --------- | -----:|\r\n" +
        "| Computer  | $1600 |\r\n" +
        "| Phone     |   $12 |\r\n" +
        "| Pipe      |    $1 |\r\n" +
        "\r\n" +
        "----\r\n" +
        "\r\n" +
        "#### 特殊符号 HTML Entities Codes\r\n" +
        "\r\n" +
        "&copy; &  &uml; &trade; &iexcl; &pound;\r\n" +
        "&amp; &lt; &gt; &yen; &euro; &reg; &plusmn; &para; &sect; &brvbar; &macr; &laquo; &middot;\r\n" +
        "\r\n" +
        "X&sup2; Y&sup3; &frac34; &frac14;  &times;  &divide;   &raquo;\r\n" +
        "\r\n" +
        "18&ordm;C  &quot;  &apos;\r\n" +
        "\r\n" +
        "#### 反斜杠 Escape\r\n" +
        "\r\n" +
        "\*literal asterisks\*\r\n" +
        "\r\n" +
        "### 科学公式 TeX(KaTeX)\r\n" +
        "\r\n" +
        "$$E=mc^2$$\r\n" +
        "\r\n" +
        "行内的公式$$E=mc^2$$行内的公式，行内的$$E=mc^2$$公式。\r\n" +
        "\r\n" +
        "$$\(\sqrt{3x-1}+(1+x)^2\)$$\r\n" +
        "\r\n" +
        "$$\sin(\alpha)^{\theta}=\sum_{i=0}^{n}(x^i + \cos(f))$$\r\n" +
        "\r\n" +
        "多行公式：\r\n" +
        "\r\n" +
        "```math\r\n" +
        "\displaystyle\r\n" +
        "\left( \sum\_{k=1}^n a\_k b\_k \right)^2\r\n" +
        "\leq\r\n" +
        "\left( \sum\_{k=1}^n a\_k^2 \right)\r\n" +
        "\left( \sum\_{k=1}^n b\_k^2 \right)\r\n" +
        "```\r\n" +
        "\r\n" +
        "```katex\r\n" +
        "\displaystyle\r\n" +
        "    \frac{1}{\r\n" +
        "        \Bigl(\sqrt{\phi \sqrt{5}}-\phi\Bigr) e^{\r\n" +
        "        \frac25 \pi}} = 1+\frac{e^{-2\pi}} {1+\frac{e^{-4\pi}} {\r\n" +
        "        1+\frac{e^{-6\pi}}\r\n" +
        "        {1+\frac{e^{-8\pi}}\r\n" +
        "         {1+\cdots} }\r\n" +
        "        }\r\n" +
        "    }\r\n" +
        "```\r\n" +
        "\r\n" +
        "```latex\r\n" +
        "f(x) = \int_{-\infty}^\infty\r\n" +
        "    \hat f(\xi)\,e^{2 \pi i \xi x}\r\n" +
        "    \,d\xi\r\n" +
        "```\r\n" +
        "\r\n" +
        "### 绘制流程图 Flowchart\r\n" +
        "\r\n" +
        "```flow\r\n" +
        "st=>start: 用户登陆\r\n" +
        "op=>operation: 登陆操作\r\n" +
        "cond=>condition: 登陆成功 Yes or No?\r\n" +
        "e=>end: 进入后台\r\n" +
        "\r\n" +
        "st->op->cond\r\n" +
        "cond(yes)->e\r\n" +
        "cond(no)->op\r\n" +
        "```\r\n" +
        "\r\n" +
        "### 绘制序列图 Sequence Diagram\r\n" +
        "\r\n" +
        "```seq\r\n" +
        "Andrew->China: Says Hello\r\n" +
        "Note right of China: China thinks\nabout it\r\n" +
        "China-->Andrew: How are you?\r\n" +
        "Andrew->>China: I am good thanks!\r\n" +
        "```\r\n" +
        "\r\n" +
        "### End\r\n" +
        "> 本网站Markdown解析器来源于国内开源项目Editor.md，谨以此来表示对该项目作者pandao大神的尊敬！";
});