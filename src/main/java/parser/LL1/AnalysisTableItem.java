package parser.LL1;

/**
 * LL(1)分析表中的元素：非终结符、终结符、下一语法的位置
 *
 * @author 软英1702 马洪升
 */
public class AnalysisTableItem {
    private char Vn;
    private char Vt;
    private int changeToNextGrammar;

    public AnalysisTableItem(char vn, char vt, int changeToNextGrammar) {
        Vn = vn;
        Vt = vt;
        this.changeToNextGrammar = changeToNextGrammar;
    }

    public char getVt() {
        return Vt;
    }

    public int getChangeToNextGrammar() {
        return changeToNextGrammar;
    }
}