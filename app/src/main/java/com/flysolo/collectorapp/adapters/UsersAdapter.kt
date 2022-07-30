package com.flysolo.collectorapp.adapters

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flysolo.collectorapp.R
import com.flysolo.collectorapp.models.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class UsersAdapter(val context: Context,val listUsers : List<User>) :
    RecyclerView.Adapter<UsersAdapter.UsersAdapterViewHolder>() {




    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UsersAdapterViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_home_owners,parent,false)
        return UsersAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersAdapterViewHolder, position: Int) {
        val user = listUsers[position]
        if (user.userProfile!!.isNotEmpty()) {
            Picasso.get().load(user.userProfile).into(holder.imageProfile)
        }
        holder.textName.text = "${user.userFirstName} ${user.userFirstName}"
        holder.textAddress.text = user.userAddress
        holder.textPhone.text = user.userPhoneNumber
    }

    override fun getItemCount(): Int {
        return listUsers.size
    }
    class UsersAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageProfile : CircleImageView = itemView.findViewById(R.id.imageProfile)
        val textName :TextView = itemView.findViewById(R.id.textName)
        val textAddress :TextView = itemView.findViewById(R.id.textAddress)
        val textPhone :TextView = itemView.findViewById(R.id.textPhoneNumber)
    }


}