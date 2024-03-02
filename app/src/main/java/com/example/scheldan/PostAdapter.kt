package com.example.scheldan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.scheldan.databinding.PostcardBinding

class PostViewHolder(
    private val binding: PostcardBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post,listener: PostAdapter.Listener){
        binding.apply {
            tautor.text = post.author
            tcontent.text = post.content
            textView4.text = post.amountlike.toString()
            textView6.text = post.sharecount.toString()
            textView2.text = post.link
            imageButton2.setBackgroundResource(if (post.likedByMe) R.drawable.lll else R.drawable.pngkkk )
            imageButton2.setOnClickListener{
                listener.onClickLike(post)
            }
            imageButton3.setOnClickListener{
                listener.onClickShare(post)
            }
            menu.setOnClickListener {
                listener.onClickMore(post, it)

            }

        }
    }
}
class PostDiffcallback : DiffUtil.ItemCallback<Post>(){
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}
class PostAdapter(
    private val listener: Listener
):androidx.recyclerview.widget.ListAdapter<Post, PostViewHolder>(PostDiffcallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        var binding = PostcardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post, listener)
    }

    interface Listener{
        fun onClickLike(post: Post)
        fun onClickShare(post: Post)
        fun onClickMore(post: Post, view: View)
        fun onClickEdit(post: Post, view: View)
        fun onClickRemove(post: Post)
    }


}
private fun convertToString(count:Int):String
{
    return when(count){
        in 0 .. 1_000 -> count.toString()
        in 1000 .. 1_100-> "1k"
        in 1_100 .. 10_000 -> ((count/100).toFloat()/10).toString() + "k"
        in 10_000 .. 1_000_000 -> (count/1_000).toString() + "k"
        in 1_000_000 .. 1_100_000 -> "1M"
        in 1_100_000 .. 10_000_000 -> ((count/100_000).toFloat()/10).toString() + "M"
        in 10_000_000 .. 1_000_000_000 -> (count/1_000_000).toString() + "M"
        else -> "Bolwe MLRD"
    }
}