import java.awt.*
import java.awt.event.ActionListener
import java.awt.event.ItemListener
import javax.swing.*

import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder

class MainWindow : JFrame() {
    private val strStyle = arrayOf("无填充", "纯色", "水平渐变", "水平渐变2", "垂直渐变", "垂直渐变2", "左上到右下", "右下到左上", "左下到右上", "右上到左下")

    private val fileChooser: JFileChooser = JFileChooser()

    private val newFile = ActionListener {
        title = "画图"
        if (panelCanvas.hasChange) {
            if (JOptionPane.showConfirmDialog(this, "是否保存修改?") == JOptionPane.OK_OPTION) {
                saveFile.actionPerformed(null);
            }
        }
        panelCanvas.newFile()
    }

    private val openFile = ActionListener {
        if (panelCanvas.hasChange) {
            if (JOptionPane.showConfirmDialog(this, "是否保存修改?") == JOptionPane.OK_OPTION) {
                saveFile.actionPerformed(null);
            }
        }
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            val file = fileChooser.selectedFile
            title = "画图 - " + file.absolutePath
            panelCanvas.loadFile(file)
        }
    }

    private val saveFile = ActionListener {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            val file = fileChooser.selectedFile
            title = "画图 - " + file.absolutePath
            panelCanvas.saveFile(file)
        }
    }

    private val exit = ActionListener {
        if (panelCanvas.hasChange) {
            if (JOptionPane.showConfirmDialog(this, "是否保存修改?") == JOptionPane.OK_OPTION) {
                saveFile.actionPerformed(null);
            }
        }
        dispose()
    }

    private val selectLineColor = ActionListener {
        val color = JColorChooser.showDialog(this, "选择线条颜色", btnLineColor.background)
        if (color != null) {
            btnLineColor.background = color
            panelCanvas.lineColor = color
        }
    }

    private var selectFillColor = ActionListener {
        val color = JColorChooser.showDialog(this, "选择填充颜色", btnFillColor.background)
        if (color != null) {
            btnFillColor.background = (color)
            panelCanvas.fillColor = color
        }
    }

    private var selectFillColor2 = ActionListener {
        val color = JColorChooser.showDialog(this, "选择填充颜色", btnFillColor2.background)
        if (color != null) {
            btnFillColor2.background = (color)
            panelCanvas.fillColor2 = color
        }
    }

    private val setLineWidth = ActionListener {
        val w = JOptionPane.showInputDialog("输入线宽", 1)
        try {
            val width = Integer.parseInt(w)
            panelCanvas.lineWidth = width
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private val aboutProgram = ActionListener {
        JOptionPane.showMessageDialog(this, "画图 Ver 1.0", "关于画图", JOptionPane.PLAIN_MESSAGE)
    }

    private val selectFillStyle = ActionListener {
        panelCanvas.fillStyle = comboStyle.selectedIndex
    }

    private val selectShape = ItemListener {
        val sel = it.itemSelectable
        when (sel) {
            btnArrow -> panelCanvas.type = DrawPanel.NONE
            btnLine -> panelCanvas.type = DrawPanel.LINE
            btnRectangle -> panelCanvas.type = DrawPanel.RECTANGLE
            btnOval -> panelCanvas.type = DrawPanel.OVAL
        }
    }

    private val menuBar: JMenuBar = JMenuBar().apply {
        add(JMenu("文件").apply {
            add(JMenuItem("新建").apply { addActionListener(newFile) })
            add(JMenuItem("打开").apply { addActionListener(openFile) })
            add(JMenuItem("保存").apply { addActionListener(saveFile) })
            add(JMenuItem("退出").apply { addActionListener(exit) })
        })
        add(JMenu("编辑").apply {
            add(JMenuItem("剪切").apply { addActionListener { panelCanvas.cut() } })
            add(JMenuItem("复制").apply { addActionListener { panelCanvas.copy() } })
            add(JMenuItem("粘贴").apply { addActionListener { panelCanvas.paste() } })
            add(JSeparator())
            add(JMenuItem("置顶").apply { addActionListener { panelCanvas.goTop() } })
            add(JMenuItem("置底").apply { addActionListener { panelCanvas.goBottom() } })
            add(JMenuItem("上移").apply { addActionListener { panelCanvas.goUp() } })
            add(JMenuItem("下移").apply { addActionListener { panelCanvas.goDown() } })
        })
        add(JMenu("帮助").apply {
            add(JMenuItem("关于").apply { addActionListener(aboutProgram) })
        })
    }

    private val btnArrow: JRadioButton = JRadioButton("鼠标").apply {
        isSelected = true
        addItemListener(selectShape)
    }

    private val btnLine: JRadioButton = JRadioButton("直线").apply { addItemListener(selectShape) }

    private val btnRectangle: JRadioButton = JRadioButton("矩形").apply { addItemListener(selectShape) }

    private val btnOval: JRadioButton = JRadioButton("椭圆").apply { addItemListener(selectShape) }

    private val groupShape: ButtonGroup = ButtonGroup().apply {
        add(btnArrow)
        add(btnLine)
        add(btnRectangle)
        add(btnOval)
    }

    private val btnLineColor: JButton = JButton("线条颜色").apply {
        addActionListener(selectLineColor)
    }

    private val btnFillColor: JButton = JButton("填充颜色").apply {
        addActionListener(selectFillColor)
    }

    private val btnFillColor2: JButton = JButton("填充颜色2").apply {
        addActionListener(selectFillColor2)
    }

    private val comboStyle: JComboBox<String> = JComboBox(strStyle).apply {
        addActionListener(selectFillStyle)
    }

    private val panelCanvas: DrawPanel = DrawPanel().apply {
        background = Color.WHITE
        bounds = Rectangle(100, 50, 500, 400)
        border = LineBorder(Color.BLACK)
    }

    private val splitPane: JSplitPane = JSplitPane().apply {
        leftComponent = JPanel(FlowLayout()).apply {
            add(JPanel(GridLayout(0, 1, 0, 0)).apply {
                border = TitledBorder("工具")
                add(btnArrow)
                add(btnLine)
                add(btnRectangle)
                add(btnOval)
                add(btnLineColor)
                add(btnFillColor)
                add(btnFillColor2)
                add(comboStyle)
            })
        }
        rightComponent = JScrollPane(JPanel(null).apply {
            add(panelCanvas)
            background = Color.GRAY
            preferredSize = Dimension(700, 500)
        })
    }

    init {
        title = "画图"
        defaultCloseOperation = DISPOSE_ON_CLOSE
        setSize(800, 600)
        jMenuBar = menuBar
        add(splitPane)
    }
}

fun main(args: Array<String>) {
    try {
        UIManager.setLookAndFeel(javax.swing.plaf.nimbus.NimbusLookAndFeel::class.java.name)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    val window = MainWindow()
    window.isVisible = true
}