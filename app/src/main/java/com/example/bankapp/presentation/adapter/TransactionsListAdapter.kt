package com.example.bankapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bankapp.common.loadImage
import com.example.bankapp.data.local.entity.TransactionModel
import com.example.bankapp.databinding.ItemTransctionlistBinding

class TransactionsListAdapter(val list: List<TransactionModel>, val onClick: (Int) -> Unit) :
    RecyclerView.Adapter<TransactionsListAdapter.ForeCastHolder>() {

    private lateinit var binding: ItemTransctionlistBinding

    inner class ForeCastHolder(val binding: ItemTransctionlistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TransactionModel) {
            with(binding) {
                tvReferenceNo.text = item.referenceId.toString()
                tvAmount.text = item.amount
                tvReceiverName.text = item.receiverName
                tvBankName.text = item.receiverBank

                item.receiverAvatar.let {
                    ivPerson.loadImage(item.receiverAvatar)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForeCastHolder {
        binding =
            ItemTransctionlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ForeCastHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ForeCastHolder, position: Int) {
        list[holder.absoluteAdapterPosition].let { holder.bind(it) }
    }

}