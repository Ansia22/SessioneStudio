package com.example.esempio.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filterable
import android.widget.TextView
import com.example.esempio.R
import java.util.logging.Filter

class MailAdapter(private val context: Context, private val mailList: List<String>) : BaseAdapter(),
    Filterable {
    private var filteredMailList: List<String> = mailList

    override fun getCount(): Int {
        return filteredMailList.size
    }

    override fun getItem(position: Int): Any {
        return filteredMailList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_mail, parent, false)
        view.findViewById<TextView>(R.id.textViewMail).text = filteredMailList[position]
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                if (constraint == null || constraint.isEmpty()) {
                    results.values = mailList
                } else {
                    val filtered = mailList.filter { it.contains(constraint, ignoreCase = true) }
                    results.values = filtered
                }
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredMailList = results?.values as List<String>
                notifyDataSetChanged()
            }
        }
    }
}