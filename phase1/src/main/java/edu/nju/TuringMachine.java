package edu.nju;

import java.util.*;

/**
 * @Author: pkun
 * @CreateTime: 2021-05-23 16:15
 */
public class TuringMachine {
    // 状态集合
    Set<String> Q=new LinkedHashSet<>();
    // 输入符号集
    Set<Character> S=new LinkedHashSet<>();
    // 磁带符号集
    Set<Character> G=new LinkedHashSet<>();
    // 初始状态
    String q;
    // 终止状态集
    Set<String> F=new LinkedHashSet<>();
    // 空格符号
    Character B;
    // 磁带数
    Integer tapeNum;
    // 迁移函数集
    Set<TransitionFunction> Delta=new LinkedHashSet<>();
    String result="";
    int line=1;
    ArrayList<String> all_kinds_of_errs=new ArrayList<String>();
    ArrayList<String> possible_output=new ArrayList<String>();
    Set<String> real_err=new LinkedHashSet<String>();
    String error="";
    String target;
    Set<String> err_message=new LinkedHashSet<String>();

    ArrayList<String> input=new ArrayList<String>();
    public TuringMachine(Set<String> Q, Set<Character> S, Set<Character> G, String q, Set<String> F, char B, int tapeNum, Set<TransitionFunction> Delta) {
        this.Q = Q;
        this.S = S;
        this.G = G;
        this.q = q;
        this.F = F;
        this.B = B;
        this.tapeNum = tapeNum;
        this.Delta = Delta;
    }
    public void err_of_kuohao(){
        int counter=1;
        for(String ele:input){
            if((ele.startsWith("#Q")||ele.startsWith("#S")||ele.startsWith("#G")||ele.startsWith("#F"))&&(!ele.contains("{")||!ele.contains("}"))){
                all_kinds_of_errs.add("Error: "+(counter+""));
            }
            counter++;
        }
    }
    public void err_of_syntax(){
        int counter=0;
        for(String ele_total:input){
            if(ele_total.startsWith("#Q")){
                if(Q.size()!=0){
                    for(String ele:Q){
                        String judge_Q[]=ele.split("");
                        for(String ele1:judge_Q){
                            if(!(ele1.charAt(0)>='a')&&!(ele1.charAt(0)<='z')&&!(ele1.charAt(0)>='A')&&!(ele1.charAt(0)<='0')&&!(ele1.charAt(0)>='9')&&!ele1.equals("_")){
                                all_kinds_of_errs.add("Error: "+((counter+1)+""));
                            }
                        }
                    }
                }
            }
            if(ele_total.startsWith("#S")){
                if(S.size()!=0){
                    for(Character ele:S){
                        if(ele.equals(' ')||ele.equals(',')||ele.equals(';')||ele.equals('*')||ele.equals('{')||ele.equals('}')||ele.equals("_")){
                            all_kinds_of_errs.add("Error: "+((counter+1)+""));
                        }
                    }
                }
            }
            if(ele_total.startsWith("#G")){
                if(G.size()!=0){
                    for(Character ele:G){
                        if(ele.equals(' ')||ele.equals(',')||ele.equals(';')||ele.equals('*')||ele.equals('{')||ele.equals('}')||ele.equals("_")){
                            all_kinds_of_errs.add("Error: "+((counter+1)+""));
                        }
                    }
                }
            }
            if(ele_total.startsWith("#F")){
                if(F.size()!=0){
                    for(String ele:F){
                        String judge_F[]=ele.split("");
                        for(String ele1:judge_F){
                            if(!(ele1.charAt(0)>='a')&&!(ele1.charAt(0)<='z')&&!(ele1.charAt(0)>='A')&&!(ele1.charAt(0)<='0')&&!(ele1.charAt(0)>='9')&&!ele1.equals("_")){
                                all_kinds_of_errs.add("Error: "+((counter+1)+""));
                            }
                        }
                    }
                }
            }
            if(ele_total.startsWith("#q0")){
                if(q!=null){
                    String[] judge_q=q.split("");
                    for(String ele1:judge_q){
                        if(!(ele1.charAt(0)>='a')&&!(ele1.charAt(0)<='z')&&!(ele1.charAt(0)>='A')&&!(ele1.charAt(0)<='0')&&!(ele1.charAt(0)>='9')&&!ele1.equals("_")){
                            all_kinds_of_errs.add("Error: "+((counter+1)+""));
                        }
                    }
                }
            }
            if(ele_total.startsWith("#B")){
                if(B!=null){
                    if(!((B+"").equals("_"))){
                        all_kinds_of_errs.add("Error: "+((counter+1)+""));
                    }
                }
            }
            if(ele_total.startsWith("#D")){
                if(Delta.size()!=0){
                    for(TransitionFunction ele:Delta){
                        if(ele.getInput().length()!=ele.getOutput().length()){
                            all_kinds_of_errs.add("Error: "+((counter+1)+""));
                        }
                    }
                }
            }
            if(!ele_total.startsWith("#Q")&&!ele_total.startsWith("#S")&&!ele_total.startsWith("#G")&&!ele_total.startsWith("#N")&&!ele_total.startsWith("#F")&&!ele_total.startsWith("#q0")&&!ele_total.startsWith("#B")&&!ele_total.startsWith("#D")){
                all_kinds_of_errs.add("Error: "+((counter+1)+""));
            }
            counter++;
        }
    }
    public void err_of_lack(){
        int counter_Q=0;
        int counter_S=0;
        int counter_G=0;
        int counter_N=0;
        int counter_F=0;
        int counter_q0=0;
        int counter_B=0;
        int counter_D=0;
        for(String ele:input){
            if(ele.startsWith("#Q")){
                counter_Q++;
            }
            if(ele.startsWith("#S")){
                counter_S++;
            }
            if(ele.startsWith("#G")){
                counter_G++;
            }
            if(ele.startsWith("#N")){
                counter_N++;
            }
            if(ele.startsWith("#F")){
                counter_F++;
            }
            if(ele.startsWith("#q0")){
                counter_q0++;
            }
            if(ele.startsWith("#B")){
                counter_B++;
            }
            if(ele.startsWith("#D")){
                counter_D++;
            }
        }
        if(counter_Q==0){
            all_kinds_of_errs.add("Error: lack "+"Q");
        }
        if(counter_S==0){
            all_kinds_of_errs.add("Error: lack "+"S");
        }
        if(counter_G==0){
            all_kinds_of_errs.add("Error: lack "+"G");
        }
        if(counter_N==0){
            all_kinds_of_errs.add("Error: lack "+"N");
        }
        if(counter_F==0){
            all_kinds_of_errs.add("Error: lack "+"F");
        }
        if(counter_q0==0){
            all_kinds_of_errs.add("Error: lack "+"q0");
        }
        if(counter_B==0){
            all_kinds_of_errs.add("Error: lack "+"B");
        }
        if(counter_D==0){
            all_kinds_of_errs.add("Error: lack "+"D");
        }
    }
    //TODO
    public TuringMachine(String tm) {
        String[] my_input;
        my_input=tm.split(System.lineSeparator());

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
                        if(counter==0){
                            if(cur_q.contains("{")){
                                Q.add(cur_q.substring(6,cur_q.length()));
                            }else {
                                Q.add(cur_q.substring(5,cur_q.length()));
                            }
                            counter++;
                        }else {
                            if(cur_q.endsWith("}")){
                                Q.add(cur_q.split("}")[0]);
                            }
                            else {
                                Q.add(cur_q);
                            }
                        }
                    }
                }else {
                    String[] cur_q=ele.split(" ");
                    if(cur_q[2].contains("{")&&!cur_q[2].contains("}")){
                        Q.add(cur_q[2].substring(1,cur_q[2].length()));
                    }
                    if(!cur_q[2].contains("{")&&cur_q[2].contains("}")){
                        Q.add(cur_q[2].substring(0,cur_q[2].length()-1));
                    }
                    if(cur_q[2].contains("{")&&cur_q[2].contains("}")){
                        Q.add(cur_q[2].substring(1,cur_q[2].length()-1));
                    }
                    if(!cur_q[2].contains("{")&&!cur_q[2].contains("}")){
                        Q.add(cur_q[2].substring(0,cur_q[2].length()));
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
                int counter=0;
                String[] cur_G=ele.split("");
                for(String G_ele:cur_G){
                    if(counter==1){
                        counter++;
                        continue;
                    }else {
                        if(!G_ele.equals(" ")&&!G_ele.equals(",")&&!G_ele.equals("#")&&!G_ele.equals("=")&&!G_ele.equals("{")&&!G_ele.equals("}")){
                            G.add(G_ele.charAt(0));
                        }
                        counter++;
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
                q=ele.split(" ")[2];
            }
            if(ele.startsWith("#B")){
                String[] cur_B=ele.split(" ");
                B=cur_B[cur_B.length-1].charAt(0);
            }
            if(ele.startsWith("#D")){
                TransitionFunction transitionFunction=new TransitionFunction(ele.substring(3,ele.length()));
                Delta.add(transitionFunction);
            }
        }
        err_of_kuohao();
        err_of_syntax();
        err_of_lack();
        get_err_message();
        if(err_message.size()!=0){
            int counter=0;
            for(String ele:err_message){
                if(counter!=err_message.size()-1){
                    error+=ele+System.lineSeparator();
                    counter++;
                }else {
                    error+=ele;
                }
            }
            System.err.print(error);
        }
    }

    //TODO
    @Override
    public String toString() {
        target=initialize_the_result();
        return target;
    }
    public String initialize_the_result(){
        if(Q.size()!=0){
            int counter1=0;
            result+="#Q = {";
            for(String ele:Q){
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
        if(q!=null){
            result+="#q0 = "+q+System.lineSeparator();
        }
        if(B!=null){
            result+="#B = "+(B+"")+System.lineSeparator();
        }
        if(Delta.size()!=0){
            for(TransitionFunction ele:Delta){
                result+=ele.toString();
            }
        }
        return result;
    }
    /**public void initialize_the_possible_output(){
        String[] output=target.split(System.lineSeparator());
        for(String ele:output){
            possible_output.add(ele);
        }
    }*/
    public void get_err_message(){
        for(String ele:all_kinds_of_errs){
            err_message.add(ele);
        }
    }
}
