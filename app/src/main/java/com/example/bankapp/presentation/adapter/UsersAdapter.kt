package com.example.bankapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bankapp.common.loadImage
import com.example.bankapp.data.local.entity.UsersModel
import com.example.bankapp.databinding.ItemUserslistBinding

class UsersAdapter(val list: List<UsersModel>, val onClick: (Int) -> Unit) :
    RecyclerView.Adapter<UsersAdapter.ForeCastHolder>() {

    private lateinit var binding: ItemUserslistBinding

    inner class ForeCastHolder(val binding: ItemUserslistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UsersModel) {
            with(binding) {
                tvName.text = item.name
                tvEmail.text = item.email
                tvBank.text = item.bankName
                item.avatar.let {
                    ivPerson.loadImage(item.avatar)
                }
                cvUser.setOnClickListener {
                    onClick(item.userId)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForeCastHolder {
        binding =
            ItemUserslistBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ForeCastHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ForeCastHolder, position: Int) {
        list[holder.absoluteAdapterPosition].let { holder.bind(it) }
    }

}