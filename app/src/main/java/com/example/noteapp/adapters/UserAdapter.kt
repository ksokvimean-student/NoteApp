package com.example.noteapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.databinding.ItemUserBinding
import com.example.noteapp.models.User

class UserAdapter(private val users:List<User>): RecyclerView.Adapter<UserAdapter.UserHolder>() {
    class UserHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserHolder(binding)
    }

    override fun onBindViewHolder(
        holder: UserHolder,
        position: Int
    ) {
        val user = users[position]
        holder.binding.textId.text = user.id.toString();
        holder.binding.textName.text = user.name;
    }

    override fun getItemCount(): Int {
        return users.size;
    }



}