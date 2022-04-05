package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainListBtnAdapter extends BaseExpandableListAdapter {
    ArrayList<ParentItem> parentItems; //부모 리스트를 담을 배열
    ArrayList<ArrayList<ChildItem>> childItems; //자식 리스트를 담을 배열



    //각 리스트의 크기 반환
    @Override
    public int getGroupCount() {
        return parentItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childItems.get(groupPosition).size();
    }

    //리스트의 아이템 반환
    @Override
    public ParentItem getGroup(int groupPosition) {
        return parentItems.get(groupPosition);
    }

    @Override
    public ChildItem getChild(int groupPosition, int childPosition) {
        return childItems.get(groupPosition).get(childPosition);
    }

    //리스트 아이템의 id 반환
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //동일한 id가 항상 동일한 개체를 참조하는지 여부를 반환
    @Override
    public boolean hasStableIds() {
        return true;
    }

    //리스트 각각의 row에 view를 설정
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        View v = convertView;
        Context context = parent.getContext();

        //convertView가 비어있을 경우 xml파일을 inflate 해줌
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.parent_list, parent, false);
        }

        //View들은 반드시 아이템 레이아웃을 inflate한 뒤에 작성할 것
        TextView section = (TextView) v.findViewById(R.id.section) ;
        TextView location = (TextView) v.findViewById(R.id.location);
        TextView ip = (TextView) v.findViewById(R.id.ipaddr);
        TextView count = (TextView) v.findViewById(R.id.spec_count);

        Button button1 = (Button) v.findViewById(R.id.status);
        button1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //status눌렀을때 실행되는 함수
                Intent intent1 = new Intent(context.getApplicationContext(),DrawStatus.class);
                intent1.putExtra("ip",getGroup(groupPosition).getIp());
                context.startActivity(intent1);

            }
        });

        Button button2 = (Button) v.findViewById(R.id.reset);
        button2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //status눌렀을때 실행되는 함수
                try {
                    new Task().execute("http://" + ip + ":50010/manage/Pc/reset").get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });


        //그룹 펼쳐짐 여부에 따라 아이콘 변경
        Log.v("child","(group) " + ip.getText());

        //리스트 아이템의 내용 설정
        section.setText(getGroup(groupPosition).getSection());
        location.setText(getGroup(groupPosition).getLocation());
        ip.setText(getGroup(groupPosition).getIp());
        count.setText(getGroup(groupPosition).getCount());

        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //Log.v("child","getChildView진입");
        View v = convertView;
        final Context context = parent.getContext();
        String temp = "";


        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.child_list, parent, false);
        }

        TextView prkey = (TextView) v.findViewById(R.id.primarykey);
        TextView devname = (TextView) v.findViewById(R.id.ddname);
        TextView pointid = (TextView) v.findViewById(R.id.pointid);


        Log.v("child","(child) group : "+groupPosition + ", child : " + childPosition);


        prkey.setText(Long.toString(getChildId(groupPosition,childPosition) + 1));
        devname.setText(getChild(groupPosition, childPosition).getPointnum());
        pointid.setText(getChild(groupPosition, childPosition).getPointid());
        Log.v("child","id"+getChildId(groupPosition,childPosition));

        //child_device = "device"+getChildId(groupPosition,childPosition);


        Button button3 = (Button) v.findViewById(R.id.feature);
        if(getChild(groupPosition, childPosition).getPointnum().contains("AE")) {
            temp = "AE"+getChild(groupPosition, childPosition).getPointid();
        }
        else if(getChild(groupPosition, childPosition).getPointnum().contains("VIB")) {
            temp = "VIB"+getChild(groupPosition, childPosition).getPointid();
        }

        String finalChild_device = temp;
        Log.v("zzz",""+ finalChild_device);
        button3.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //status눌렀을때 실행되는 함수
                Log.v("button3","눌려짐");
                if(getChild(groupPosition, childPosition).getPointnum().contains("AE")){
                    Log.v("but","AE");

                    Intent intent1 = new Intent(context.getApplicationContext(), DrawAEFeature.class);
                    intent1.putExtra("ip",getGroup(groupPosition).getIp());
                    intent1.putExtra("device",finalChild_device);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent1);
                }
                else if(getChild(groupPosition, childPosition).getPointnum().contains("VIB")){
                    Log.v("but","VIB");
                    Intent intent1 = new Intent(context.getApplicationContext(), DrawVIBFeature.class);
                    intent1.putExtra("ip",getGroup(groupPosition).getIp());
                    intent1.putExtra("device",finalChild_device);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent1);
                }



            }
        });









        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        Log.v("zzzz","group : "+groupPosition + ", child : "+childPosition);
        return true;
    }

    //리스트에 새로운 아이템을 추가
    public void addItem(int groupPosition, ChildItem item) {
        childItems.get(groupPosition).add(item);
    }

    //리스트 아이템을 삭제
    public void removeChild(int groupPosition, int childPosition) {
        childItems.get(groupPosition).remove(childPosition);
    }
}