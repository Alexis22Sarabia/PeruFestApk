package edu.pe.idat.perufestapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.pe.idat.perufestapp.post.Post
import edu.pe.idat.perufestapp.databinding.PostEventosBinding

class PostAdapter(private val context: Context): RecyclerView.Adapter<PostAdapter.MainViewHolder>() {
    private var dataList = mutableListOf<Post>()
    fun setListData(data:MutableList<Post>){
        dataList = data
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
     val binding = PostEventosBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MainViewHolder(binding)
    }
    override fun getItemCount(): Int {
    return if (dataList.size>0){
        dataList.size
    }else{
        0
    }
    }
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val post = dataList[position]
        holder.bind(post)
    }
    inner class MainViewHolder(private val binding: PostEventosBinding ): RecyclerView.ViewHolder(binding.root){
        fun bind(post: Post){
            Glide.with(context).load(post.imagurl).into(binding.imagtv)
            binding.etnombreeven.text = post.nombrevento
            binding.desceventv.text = post.descripcion
            binding.fechaeventv.text = post.fechaevento
            binding.direceventv.text = post.direccionevento
            binding.eveninterestv.text = post.eventointeres
            binding.entradatv.text = post.tipoentrada
            binding.nombreusuariotv.text = post.nombreusuario
        }

    }
}
