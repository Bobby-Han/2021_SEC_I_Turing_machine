package edu.nju;

import java.util.ArrayList;
import java.util.*;
import java.util.LinkedHashSet;

/**
 * @Author: pkun
 * @CreateTime: 2021-05-25 23:53
 */
public class Executor {

    ArrayList<Tape> tapes=new ArrayList<Tape>();
    TuringMachine tm;
    State q;
    int steps = 0;
    boolean canRun = true;


    public Executor(TuringMachine tm, ArrayList<Tape> tapes) {
        this.tm = tm;
        q = tm.getInitState();
        loadTape(tapes);
    }

    /**
     * TODO
     * 1. 检查能否运行
     * 2. 调用tm.delta
     * 3. 更新磁带
     * 4. 返回下次能否执行
     *
     * @return
     */
    public Boolean execute() {
        if(canRun){
            String cur_snapshot_of_the_tape=snapShotTape();
            updateTape(q.getDelta(cur_snapshot_of_the_tape).getOutput());
            moveHeads(q.getDelta(cur_snapshot_of_the_tape).getDirection());
            q=q.getDelta(cur_snapshot_of_the_tape).getDestinationState();
        }
        if(tm.isStop(q,snapShotTape())){
            canRun=false;
        }
        return canRun;
    }

    /**
     * TODO
     * 1. 检查磁带的数量是否正确 ( checkTapeNum )
     * 2. 检查磁带上的字符是否是输入符号组的 ( checkTape )
     *
     * @param tapes
     */
    public void loadTape(ArrayList<Tape> tapes) {
        this.tapes=tapes;
        if(!tm.checkTapeNum(tapes.size())){
            System.err.println("Error: 2");
        }
        if(tapes.size()==2){
            String tape0_track0=tapes.get(0).tracks.get(0).toString();
            char[] t0_t0=tape0_track0.toCharArray();
            Set<Character> ta0_tr0=new LinkedHashSet<>();
            for(char ele:t0_t0){
                ta0_tr0.add(ele);
            }
            String tape1_track0=tapes.get(1).tracks.get(0).toString();
            char[] t1_t0=tape1_track0.toCharArray();
            Set<Character> ta1_tr0=new LinkedHashSet<>();
            for(char ele:t1_t0){
                ta1_tr0.add(ele);
            }
            if(tm.checkTape(ta0_tr0)==false||tm.checkTape(ta1_tr0)==false){
                System.err.println("Error: 1");
            }
        }
        if(tapes.size()==1){
            String tape0_track0=tapes.get(0).tracks.get(0).toString();
            char[] t0_t0=tape0_track0.toCharArray();
            Set<Character> ta0_tr0=new LinkedHashSet<>();
            for(char ele:t0_t0){
                ta0_tr0.add(ele);
            }
            if(tm.checkTape(ta0_tr0)==false){
                System.err.println("Error: 1");
            }
        }
    }

    /**
     * TODO
     * 获取所有磁带的快照，也就是把每个磁带上磁头指向的字符全都收集起来
     *
     * @return
     */
    private String snapShotTape() {
        String cur_z1="";
        if(tapes.size()==1){
            String tape0_track_0=tapes.get(0).tracks.get(0).toString();
            cur_z1+=tape0_track_0.charAt(tapes.get(0).getHead())+"";
        }
        if(tapes.size()==2){
            String tape0_track_0=tapes.get(0).tracks.get(0).toString();
            cur_z1+=tape0_track_0.charAt(tapes.get(0).getHead())+"";
            String tape1_track_0=tapes.get(1).tracks.get(0).toString();
            cur_z1+=tape1_track_0.charAt(tapes.get(1).getHead())+"";
        }
        return cur_z1;
    }

    /**
     * TODO
     * 按照README给出当前图灵机和磁带的快照
     *
     * @return
     */
    public String snapShot() {
        String res="";
        res+="Step  : "+(steps+"")+System.lineSeparator();
        steps++;
        if(tapes.size()!=1){
            for(int i=0;i<tapes.size();i++){
                res+="Tape"+(i+"")+" :"+System.lineSeparator();
                res+="Index"+(i+"")+":"+" "+tapes.get(i).get_index(tapes.get(i).tracks.get(0).toString())+System.lineSeparator();
                res+="Track0:"+" "+tapes.get(i).get_updated_string(tapes.get(i).get_index(tapes.get(i).tracks.get(0).toString()))+System.lineSeparator();
                res+="Head"+(i+"")+" :"+" "+(tapes.get(i).getHead()+"")+System.lineSeparator();
                if(i==tapes.size()-1){
                    res+="State : "+q.getQ()+System.lineSeparator();
                }
            }
        }if(tapes.size()==1){
            int i=0;
            res+="Tape"+(i+"")+" :"+System.lineSeparator();
            res+="Index0:"+" "+tapes.get(i).get_index(tapes.get(i).tracks.get(0).toString())+System.lineSeparator();
            res+="Track0:"+" "+tapes.get(i).get_updated_string(tapes.get(i).get_index(tapes.get(i).tracks.get(0).toString()))+System.lineSeparator();
            res+="Head"+(i+"")+" :"+" "+(tapes.get(i).getHead()+"")+System.lineSeparator();
            res+="State : "+q.getQ()+System.lineSeparator();
        }
        return res;
    }


    /**
     * TODO
     * 不断切割newTapes，传递给每个Tape的updateTape方法
     *
     * @param newTapes
     */
    private void updateTape(String newTapes) {
        if(tapes.size()==2){
            tapes.get(0).updateTape(newTapes.charAt(0)+"");
            tapes.get(1).updateTape(newTapes.charAt(1)+"");
        }
        if(tapes.size()==1){
            tapes.get(0).updateTape(newTapes);
        }
    }

    /**
     * TODO
     * 将每个direction里的char都分配给Tape的updateHead方法
     *
     * @param direction
     */
    private void moveHeads(String direction) {
        if (tapes.size() == 2) {
            tapes.get(0).updateHead(direction.charAt(0));
            tapes.get(1).updateHead(direction.charAt(1));
        }
        if (tapes.size() == 1) {
            tapes.get(0).updateHead(direction.charAt(0));
        }
    }
}
