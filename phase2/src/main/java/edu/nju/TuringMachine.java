package edu.nju;

import java.awt.font.TransformAttribute;
import java.awt.image.AreaAveragingScaleFilter;
import java.util.*;

/**
 * @Author: pkun
 * @CreateTime: 2021-05-23 16:15
 */
public class TuringMachine {

    // 状态集合
    private final Map<String, State> Q;
    // 输入符号集
    private Set<Character> S=new LinkedHashSet<>();;
    // 磁带符号集
    private Set<Character> G=new LinkedHashSet<>();;
    // 初始状态
    private String q0;
    // 终止状态集
    private Set<String> F=new LinkedHashSet<>();;
    // 空格符号
    private Character B;
    // 磁带数
    private Integer tapeNum;
    ArrayList<String> input=new ArrayList<String>();
    String result="";
    static ArrayList<State> delta_init_state=new ArrayList<State>();
    static ArrayList<String> delta_has_the_same_input=new ArrayList<String>();
    static ArrayList<String> same_input_output=new ArrayList<String>();
    static ArrayList<String> same_input_direction=new ArrayList<String>();
    static ArrayList<State> same_input_state=new ArrayList<State>();



    public TuringMachine(Set<String> Q, Set<Character> S, Set<Character> G, String q, Set<String> F, char B, int tapeNum, Set<TransitionFunction> Delta) {
        this.S = S;
        this.G = G;
        this.F = F;
        this.B = B;
        this.q0 = q;
        this.Q = new HashMap<>();
        for (String state : Q) {
            State temp = new State(state);
            temp.setQ(state);
            this.Q.put(state, temp);
        }
        this.tapeNum = tapeNum;
        for (TransitionFunction t : Delta) {
            this.Q.get(t.getSourceState().getQ()).addTransitionFunction(t);
        }
    }

    /**
     * TODO
     * is done in Lab1 ~
     *
     * @param tm
     */
    public TuringMachine(String tm) {

        Q = new HashMap<>();
        String[] my_input;
        my_input=tm.split(System.lineSeparator());
        //删除注释
        for(String ele:my_input){
            String new_ele=ele.trim();
            if(new_ele.length()==0){
                continue;
            }else {
                if((new_ele.charAt(0)+"").equals(";")){
                    continue;
                }
                else {
                    input.add(new_ele);
                }
            }
        }
        for(String ele:input){
            if(ele.startsWith("#Q")){
                if(ele.contains(",")){
                    int counter=0;
                    String[] cur_Q=ele.split(",");
                    for(String cur_q:cur_Q){
                        cur_q=cur_q.trim();
                        if(counter==0){
                            if(cur_q.contains("{")){
                                State temp=new State(cur_q.substring(6,cur_q.length()));
                                temp.setQ(cur_q.substring(6,cur_q.length()));
                                Q.put(cur_q.substring(6,cur_q.length()),temp);
                            }else {
                                State temp=new State(cur_q.substring(5,cur_q.length()));
                                temp.setQ(cur_q.substring(5,cur_q.length()));
                                Q.put(cur_q.substring(5,cur_q.length()),temp);
                            }
                            counter++;
                        }else {
                            if(cur_q.endsWith("}")){
                                State temp=new State(cur_q.split("}")[0]);
                                temp.setQ(cur_q.split("}")[0]);
                                Q.put(cur_q.split("}")[0],temp);
                            }
                            else {
                                State temp=new State(cur_q);
                                temp.setQ(cur_q);
                                Q.put(cur_q,temp);
                            }
                        }
                    }
                }else {
                    String[] cur_q=ele.split(" ");
                    if(cur_q[2].contains("{")&&!cur_q[2].contains("}")){
                        State temp=new State(cur_q[2].substring(1,cur_q[2].length()));
                        temp.setQ(cur_q[2].substring(1,cur_q[2].length()));
                        Q.put(cur_q[2].substring(1,cur_q[2].length()),temp);
                    }
                    if(!cur_q[2].contains("{")&&cur_q[2].contains("}")){
                        State temp=new State(cur_q[2].substring(0,cur_q[2].length()-1));
                        temp.setQ(cur_q[2].substring(0,cur_q[2].length()-1));
                        Q.put(cur_q[2].substring(0,cur_q[2].length()-1),temp);
                    }
                    if(cur_q[2].contains("{")&&cur_q[2].contains("}")){
                        State temp=new State(cur_q[2].substring(1,cur_q[2].length()-1));
                        temp.setQ(cur_q[2].substring(1,cur_q[2].length()-1));
                        Q.put(cur_q[2].substring(1,cur_q[2].length()-1),temp);

                    }
                    if(!cur_q[2].contains("{")&&!cur_q[2].contains("}")){
                        State temp=new State(cur_q[2].substring(0,cur_q[2].length()));
                        temp.setQ(cur_q[2].substring(0,cur_q[2].length()));
                        Q.put(cur_q[2].substring(0,cur_q[2].length()),temp);
                    }
                }
            }
            if(ele.startsWith("#S")){
                int counter=0;
                String[] cur_S=ele.split("");
                for(String s_ele:cur_S){
                    if(counter==1){
                        counter++;
                        continue;
                    }else {
                        if(!s_ele.equals(" ")&&!s_ele.equals(",")&&!s_ele.equals("#")&&!s_ele.equals("=")&&!s_ele.equals("{")&&!s_ele.equals("}")){
                            S.add(s_ele.charAt(0));
                        }
                        counter++;
                    }
                }

            }
            if(ele.startsWith("#G")){
                for(int k=6;k<ele.length();k++){
                    if(ele.charAt(k)==','){
                        continue;
                    }else {
                        G.add(ele.charAt(k));
                    }
                }
            }
            if(ele.startsWith("#F")){
                if(ele.contains(",")){
                    int counter=0;
                    String[] cur_f=ele.split(",");
                    for(String cur_F:cur_f){
                        if(counter==0){
                            if(cur_F.contains("{")){
                                F.add(cur_F.substring(6,cur_F.length()));
                            }else {
                                F.add(cur_F.substring(5,cur_F.length()));
                            }
                            counter++;
                        }else {
                            if(cur_F.endsWith("}")){
                                F.add(cur_F.split("}")[0]);
                            }
                            else {
                                F.add(cur_F);
                            }
                        }
                    }
                }
                else {
                    String[] cur_f=ele.split(" ");
                    if(cur_f[2].contains("{")&&!cur_f[2].contains("}")){
                        F.add(cur_f[2].substring(1,cur_f[2].length()));
                    }
                    if(!cur_f[2].contains("{")&&cur_f[2].contains("}")){
                        F.add(cur_f[2].substring(0,cur_f[2].length()-1));
                    }
                    if(cur_f[2].contains("{")&&cur_f[2].contains("}")){
                        F.add(cur_f[2].substring(1,cur_f[2].length()-1));
                    }
                    if(!cur_f[2].contains("{")&&!cur_f[2].contains("}")){
                        F.add(cur_f[2].substring(0,cur_f[2].length()));
                    }
                }
            }
            if(ele.startsWith("#N")){
                String[] cur_N=ele.split(" ");
                tapeNum=Integer.parseInt(cur_N[cur_N.length-1]);
            }
            if(ele.startsWith("#q0")){
                q0=ele.split(" ")[2];
            }
            if(ele.startsWith("#B")){
                String[] cur_B=ele.split(" ");
                B=cur_B[cur_B.length-1].charAt(0);
            }
            if(ele.startsWith("#D")){
                TransitionFunction delta=new TransitionFunction(ele.substring(3,ele.length()),Q);
                delta_init_state.add(delta.getSourceState());
                delta_has_the_same_input.add(delta.getInput());
                same_input_direction.add(delta.getDirection());
                same_input_output.add(delta.getOutput());
                same_input_state.add(delta.getDestinationState());
                Q.get(delta.getSourceState().getQ()).addTransitionFunction(delta);
            }
        }
        err3();
        err4();
        err5();
        err6();
        err7();
        err8();
        err9();
    }
    public void err3(){
        Collection<String> Q_key=Q.keySet();
        for(String ele:F){
            if(!Q_key.contains(ele)){
                System.err.println("Error: 3");
                break;
            }
        }
    }
    public void err4(){
        if(S.contains(B)){
            System.err.println("Error: 4");
        }
    }
    public void err5(){
        if(!G.contains(B)){
            System.err.println("Error: 5");
        }
    }
    public void err6(){
        if(!G.containsAll(S)){
            System.err.println("Error: 6");
        }
    }
    public void err7(){
        Collection<State> all_state=Q.values();
        for(State ele:all_state){
            if(ele.getDeltas()!=null){
                Collection<TransitionFunction> cur=ele.getDeltas();
                for(TransitionFunction ele1:cur){
                    if(!all_state.contains(ele1.getSourceState())){
                        System.err.println("Error: 7");
                    }
                    if(!all_state.contains(ele1.getDestinationState())){
                        System.err.println("Error: 7");
                    }
                }
            }
        }
    }
    public void err8(){
        Collection<State> all_state=Q.values();
        for(State ele:all_state){
            Collection<TransitionFunction> cur=ele.getDeltas();
            for(TransitionFunction transitionFunction:cur){
                for(int i=0;i<transitionFunction.getInput().length();i++){
                    if(!G.contains(transitionFunction.getInput().charAt(i))) {
                        System.err.println("Error: 8");
                        break;
                    }
                }
                for(int j=0;j<transitionFunction.getOutput().length();j++){
                    if(!G.contains(transitionFunction.getOutput().charAt(j))){
                        System.err.println("Error: 8");
                        break;
                    }
                }
            }
        }
    }
    public void err9(){
        for(int i=0;i<delta_has_the_same_input.size()-1;i++){
            for(int j=i+1;j<delta_has_the_same_input.size();j++){
                if((delta_has_the_same_input.get(i).equals(delta_has_the_same_input.get(j)))&&(delta_init_state.get(i).equals(delta_init_state.get(j)))){
                    if(!same_input_direction.get(i).equals(same_input_direction.get(j))){
                        System.err.println("Error: 9");
                    }
                    if(!same_input_state.get(i).equals(same_input_state.get(j))){
                        System.err.println("Error: 9");
                    }
                    if(!same_input_output.get(i).equals(same_input_output.get(j))){
                        System.err.println("Error: 9");
                    }
                }
            }
        }
    }

    public State getInitState() {
        return Q.get(q0);
    }
    public State getfinalstate(){
        String final_state="";
        for(String ele:F){
            final_state+=ele;
        }
        return Q.get(final_state);
    }

    /**
     * TODO
     * 停止的两个条件 1. 到了终止态 2. 无路可走，halts
     *
     * @param q Z
     * @return
     */

    public boolean isStop(State q, String Z) {
        if(q.equals(getfinalstate())){
            return true;
        }
        if(!q.equals(getfinalstate())){
            int counter=0;
            Collection<TransitionFunction> q_tra=q.getDeltas();
            for(TransitionFunction ele:q_tra){
                if(ele.getInput().equals(Z)){
                    counter++;
                }
            }
            if(counter==0){
                return true;
            }
        }
        return false;
    }

    public boolean checkTape(Set<Character> tape) {
        for(Character ele:tape){
            if(ele=='_'){
                continue;
            }else {
                if(!S.contains(ele)){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkTapeNum(int tapeNum) {
        if(tapeNum==this.tapeNum)
        {
            return true;
        }
        return false;
    }

    public Character getB() {
        return B;
    }


    /**
     * TODO
     * 检查迁移函数是否符合要求
     * @param s
     * @param lineno
     */
    private void resolverTransitionFunction(String s, int lineno) {









    }


    /**
     * TODO
     * is done in lab1 ~
     *
     * @return
     */
    @Override
    public String toString() {
        String target="";
        target=initialize_the_result();
        return target;
    }
    public String initialize_the_result(){
        if(Q.size()!=0){
            int counter1=0;
            result+="#Q = {";
            for(String ele:Q.keySet()){
                if(counter1!=Q.size()-1){
                    result+=ele+",";
                    counter1++;
                }else {
                    result+=ele+"}"+System.lineSeparator();
                }
            }
        }
        if(S.size()!=0){
            int counter2=0;
            result+="#S = {";
            for(Character ele:S){
                if(counter2!=S.size()-1){
                    result+=(ele+"")+",";
                    counter2++;
                }else {
                    result+=(ele+"")+"}"+System.lineSeparator();
                }
            }
        }
        if(G.size()!=0){
            int counter3=0;
            result+="#G = {";
            for(Character ele:G){
                if(counter3!=G.size()-1){
                    result+=(ele+"")+",";
                    counter3++;
                }else {
                    result+=(ele+"")+"}"+System.lineSeparator();
                }
            }
        }
        if(tapeNum!=null){
            result+="#N = "+(tapeNum+"")+System.lineSeparator();
        }
        if(F.size()!=0){
            int counter4=0;
            result+="#F = {";
            for(String ele:F){
                if(counter4!=F.size()-1){
                    result+=ele+",";
                    counter4++;
                }else {
                    result+=ele+"}"+System.lineSeparator();
                }
            }
        }
        if(q0!=null){
            result+="#q0 = "+q0+System.lineSeparator();
        }
        if(B!=null){
            result+="#B = "+(B+"")+System.lineSeparator();
        }
        for(State ele:Q.values()){
            Collection<TransitionFunction> cur=ele.getDeltas();
            for(TransitionFunction ele1:cur){
                result+=ele1.toString();
            }
        }
        return result;
    }

}
