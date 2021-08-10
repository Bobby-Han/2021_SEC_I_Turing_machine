package edu.nju;



import java.util.ArrayList;
import java.util.Collections;

/**
 * @Author: pkun
 * @CreateTime: 2021-05-23 19:37
 */
public class Tape {

    public  ArrayList<StringBuilder> tracks=new ArrayList<StringBuilder>();
    private final char B;
    private int head;

    public Tape(ArrayList<StringBuilder> tracks, int head, char B) {
        this.tracks = tracks;
        this.head = head;
        this.B = B;
    }

    public String snapShot() {
        return null;
    }

    public void updateHead(char c) {
        if(c=='r'){
            head++;
            if(head>=tracks.get(0).length()){
                tracks.get(0).append("_");
            }
        }
        if (c=='l'){
            head--;
            if(head<0){
                tracks.get(0).insert(0,"_");
                head=0;
            }
        }

    }
    public void updateTape(String newTape) {
        char[] cur_track=tracks.get(0).toString().toCharArray();
        cur_track[head]=newTape.charAt(0);
        String cur="";
        cur=String.valueOf(cur_track);
        StringBuilder temp=new StringBuilder();
        temp.append(cur);
        tracks.set(0,temp);
    }

    public String get_updated_string(String newTape){
        String[] index=newTape.split(" ");
        String ans="";
        for(int i=0;i<index.length;i++){
            int space_length=index[i].length();
            ans+=(tracks.get(0).toString().charAt(Integer.parseInt(index[i]))+"")+String.join("", Collections.nCopies(space_length," "));
        }
        return ans.trim();
    }
    public String get_index(String newTape){
        String res="";
        int pointer=head;
        int pointer2=head;
        for(int i=0;i<newTape.length();i++){
            if(newTape.charAt(i)!='_'){
               pointer2=i;
               break;
            }
        }
        for(int j=newTape.length()-1;j>=0;j--){
            if(newTape.charAt(j)!='_'){
                pointer=j;
                break;
            }
        }
        if(tracks.get(0).toString().charAt(head)=='_'){
            if(head<=pointer2){
                for(int k=head;k<=pointer;k++){
                    res+=(k+"")+" ";
                }
            }
            if(head>pointer2&&head<=pointer){
                for(int k=pointer2;k<=pointer;k++){
                    res+=(k+"")+" ";
                }
            }
            if(head>pointer){
                for(int k=pointer2;k<=head;k++){
                    res+=(k+"")+" ";
                }
            }
        }
        else {
            for(int k=pointer2;k<=pointer;k++){
                res+=(k+"")+" ";
            }
        }

        res=res.trim();
        return res;
    }
    public int getHead(){
        return head;
    }


}
