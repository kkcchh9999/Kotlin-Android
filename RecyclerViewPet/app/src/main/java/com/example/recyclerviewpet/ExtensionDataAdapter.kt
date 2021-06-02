package com.example.recyclerviewpet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.data_list_item.*

class ExtensionDataAdapter( //ArrayList, 실행 제어? , 람다식 으로 선언
    val items: ArrayList<Pet>,
    val context: Context,
    val itemSelect: (Pet) -> Unit
) : RecyclerView.Adapter<ExtensionDataAdapter.ExtensionViewHolder>() {
    override fun getItemCount(): Int {
        return items.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtensionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.data_list_item,
            parent,
            false
        )
        return ExtensionViewHolder(view, itemSelect)
    }

    override fun onBindViewHolder(holder: ExtensionViewHolder, position: Int) {
        holder.bind(items[position], context)
    }
    //사용자 뷰홀더 클래스
    inner class ExtensionViewHolder(override val containerView: View, itemSelect: (Pet) -> Unit)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
            fun bind(pet: Pet, context: Context) {  //데이터 연결
                if(pet.photo != "") {   //이미지
                    val resourceID = context.resources.getIdentifier(
                        pet.photo,
                        "drawable",
                        context.packageName
                    )
                    img_pet?.setImageResource(resourceID)
                } else {    //없으면 기본 아이콘
                    img_pet?.setImageResource(R.mipmap.ic_launcher)
                }

                tv_breed.text = pet.breed
                tv_age.text = pet.age
                tv_gender.text = pet.gender
                //람다식 매개변수를 onClick 시에 실행
                itemView.setOnClickListener() { itemSelect(pet) }
            }
        }
    }
