package com.example.howabout.Search;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.howabout.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<Document> items = null;

    //리사이클러뷰에 뿌려줄 데이터 받기
    public SearchAdapter(List<Document> documentList){
        this.items = documentList;
        Log.e("searchKeyword", "myTestAdapter: "+items.get(0));//데이터 확인 성공. 잘들어옴
    }

    //onItemClickListener 인터페이스 선언
    public interface OnItemClickListener {
        void onItemClicked(int position, View view);
    }

    //OnItemClickListener 참조 변수 선언
    public static OnItemClickListener itemClickListener = null;

    //OnItemClickListener 전달 메소드
    public void setOnItemClickListener(OnItemClickListener listener){
        this.itemClickListener = listener;
    }

    //아이템 뷰를 위한 뷰홀더 객체를 생성해 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_location, viewGroup, false);

//        //recycler view click event
//        viewGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        return new ViewHolder(view);
    }
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Context context = parent.getContext();
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        View view = inflater.inflate(R.layout.item_location, parent, false);
//        MyTestAdapter.ViewHolder viewHolder = new MyTestAdapter.ViewHolder(view);
//        return viewHolder;
//    }

    //position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.fade_in); //animation 효과 적용
        Document document = items.get(position);
        Log.e("searchKeyword", "=============아이템뷰에 표시: "+document.toString());
        holder.place_name.setText(document.getPlaceName());
        holder.category.setText(document.getCategoryGroupName());
        holder.address_name.setText(document.getAddressName());
        holder.itemView.startAnimation(animation); //animation 적용
    }

    //아이템의 개수 반환
    @Override
    public int getItemCount() {
        return items.size();
    }

    //아이템 클리어
    public void clear() {
        items.clear();
    }

    //items 정보를 뿌려줄 layout 정의??
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView place_name, address_name, category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            place_name = itemView.findViewById(R.id.ltem_location_tv_placename);
            address_name = itemView.findViewById(R.id.ltem_location_tv_address);
            category = itemView.findViewById(R.id.ltem_location_tv_category);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(itemClickListener != null){
                            itemClickListener.onItemClicked(position, itemView);
                        }
                    }
                }
            });
        }
    }
}