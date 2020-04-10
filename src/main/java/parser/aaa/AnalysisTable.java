package parser.aaa;

import java.util.ArrayList;

public class AnalysisTable {

    public String grammer[] = {"E->TY", "Y->+TgY", "Y->-TgY", "Y->n", "T->FX", "X->*FgX", "X->/FgX", "X->n", "F->ip", "F->(E)"};
    public char Vt[] = {'+', '-', '*', '/', 'i', '(', ')', '#'};
    public char Vn[] = {'E', 'T', 'Y', 'F', 'X'};
    public ArrayList<Character> first;
    public ArrayList<Character> follow;
    public ArrayList<Character> select;
    public ArrayList<AnalysisTableItem> analysisTable = new ArrayList<AnalysisTableItem>();//�ڽӱ�ʽ�洢

    public void makeAnalysisTable() {
        //�ȷ����һ�����ս��ַ���ȻJѭ����ִ��
        AnalysisTableItem analysisTableItem = new AnalysisTableItem();
        analysisTableItem.setNChar(grammer[0].split("->")[0].charAt(0));
        analysisTable.add(analysisTableItem);
        //��ʼ��������������ķ��ս�����ַ�����
        for (int i = 0; i < grammer.length; i++) {
            for (int j = 0; j < analysisTable.size(); j++) {
//				System.out.print(analysisTable.get(j).getNChar()+" ");
                //�ж��Ƿ��Ѿ����ڷ���������
                if (analysisTable.get(j).getNChar() == grammer[i].split("->")[0].charAt(0))
                    break;
                //��⵽ĩβ��û�г��ֹ������¼ӽ���������
                if (j == analysisTable.size() - 1) {
                    analysisTableItem = new AnalysisTableItem();
                    analysisTableItem.setNChar(grammer[i].split("->")[0].charAt(0));
                    analysisTable.add(analysisTableItem);
                }
            }
//			System.out.println();
        }
        //ÿ������ʽ��Ӧ��select��
        String str;
        for (int i = 0; i < grammer.length; i++) {
            first = new ArrayList<Character>();
            follow = new ArrayList<Character>();
            select = new ArrayList<Character>();
            str = grammer[i].split("->")[1];
            char followCh;
            //�����Ƶ����ռ�
            if (makeFirstSet(str)) {
                select.addAll(first);

                //Ѱ������str�Ĳ���ʽ��

                if (str.equals("n")) {
                    followCh = grammer[i].split("->")[0].charAt(0);
                    makeFollowSet(followCh);
                } else {
                    for (int k = 0; k < grammer.length; k++) {
                        if (grammer[k].split("->")[1].equals(str)) {
                            followCh = grammer[k].split("->")[0].charAt(0);
                            makeFollowSet(followCh);
                        }
                    }
                }
                select.addAll(follow);
            } else {
                select.addAll(first);
            }
            select = removeDuplicate(select);
//			System.out.println(str+":");
            //��Select���������ɷ�����
            for (int k = 0; k < analysisTable.size(); k++) {
                //�ڷ��������ҵ������Ƕ�Ӧ����߷��ս��������ͨ���󵽵�select�����ɶ�Ӧ�Ľڵ���Ϣ
                if (analysisTable.get(k).getNChar() == grammer[i].split("->")[0].charAt(0)) {
                    for (int j = 0; j < select.size(); j++) {
                        analysisTable.get(k).getNodes().add(new TableNode(select.get(j), i));
                    }
                }
            }
        }
        //��ӡ������
        System.out.println("���������£�");
        for (int i = 0; i < analysisTable.size(); i++) {
            System.out.print(analysisTable.get(i).getNChar());
            for (int j = 0; j < analysisTable.get(i).getNodes().size(); j++) {
                System.out.print("->" + analysisTable.get(i).getNodes().get(j).getType() + "  " + analysisTable.get(i).getNodes().get(j).getChangeNum());
            }
            System.out.println();
        }
    }

    //����First��ͬʱ�ж��ַ����ܷ�õ�ipslon
    private boolean makeFirstSet(String str) {
        for (int i = 0; i < str.length(); i++) {
            //�жϸ��ַ��Ƿ������ɿռ�
            if (!makeFirstChar(str.charAt(i)))
                return false;
        }
        return true;
    }

    //���һ���ַ�ͬʱ������Ƿ��ܵõ�ipslon����first��
    private boolean makeFirstChar(char ch) {
        boolean ifPIpslon = false;
        //���i�� �ս��
        if (isVt(ch)) {
            first.add(ch);
            return false;//�ò���ipslon
        } else if (ch == 'n') {
            return true;
        } else {
            for (int i = 0; i < grammer.length; i++) {
                if (grammer[i].split("->")[0].charAt(0) == ch) {
                    //��һ�����Լ���
                    if (makeFirstSet(grammer[i].split("->")[1]))
                        ifPIpslon = true;
                }
            }
            return ifPIpslon;
        }
    }

    //����follow
    private void makeFollowSet(char followCh) {
        if (followCh == 'E') {
            follow.add('#');
        }
        if (isVt(followCh))
            return;
        //�鿴�����Ҳ�����followCh�Ĳ���ʽ
        for (int i = 0; i < grammer.length; i++) {
//			System.out.println(grammer[i].split("->")[1].length());
            //��߷��ս�����ܵ�������follow����

            for (int k = 0; k < grammer[i].split("->")[1].length(); k++) {
                //�Ҳ���followCh
                if (grammer[i].split("->")[1].charAt(k) == followCh) {

                    first = new ArrayList<Character>();
                    //followCh���ǲ���ʽ���һ��
                    if (k < grammer[i].split("->")[1].length() - 1) {
                        //followCh����Ŀ����Ƶ����ռ�
                        if (makeFirstChar(grammer[i].split("->")[1].charAt(k + 1))) {
                            follow.addAll(first);
                            makeFollowSet(grammer[i].split("->")[1].charAt(k + 1));
                        } else {
                            follow.addAll(first);
                        }
                    } else {
                        //�ص㣺�ڽ���ƥ���ʱ���жϱ���������޵ݹ�
                        if (grammer[i].split("->")[0].charAt(0) != followCh) {
                            makeFollowSet(grammer[i].split("->")[0].charAt(0));
                        }
                    }
                }
            }
        }

    }

    //�Ƿ�����Vt
    private boolean isVt(char valueOf) {
        for (int i = 0; i < Vt.length; i++) {
            if (Vt[i] == valueOf)
                return true;
        }
        return false;
    }

    public ArrayList<Character> removeDuplicate(ArrayList<Character> list) {
        for (int i = 0; i < list.size() - 1; i++) {   //��������ѭ��
            for (int j = list.size() - 1; j > i; j--) {  //����������ѭ��
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);                              //������Ƴ�
                }
            }
        }
        return list;
    }
}
