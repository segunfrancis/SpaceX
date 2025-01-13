package com.segunfrancis.spacex.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.segunfrancis.spacex.R
import com.segunfrancis.spacex.databinding.ItemCompanyInfoBinding

class CompanyInfoAdapter(private val companyInfo: String) :
    RecyclerView.Adapter<CompanyInfoAdapter.CompanyInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyInfoViewHolder {
        return CompanyInfoViewHolder(
            ItemCompanyInfoBinding.bind(
                LayoutInflater.from(parent.context).inflate(R.layout.item_company_info, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: CompanyInfoViewHolder, position: Int) {
        holder.bind(companyInfo)
    }

    override fun getItemCount(): Int = 1

    inner class CompanyInfoViewHolder(private val binding: ItemCompanyInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(companyInfo: String) = with(binding) {
            companyInfoText.text = companyInfo
        }
    }
}
