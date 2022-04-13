package com.nzf.turbozakat

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.nzf.turbozakat.AssetActivity.Companion.KEY_ASSET
import com.nzf.turbozakat.AssetActivity.Companion.KEY_NEXT_QUESTION
import com.nzf.turbozakat.AssetActivity.Companion.REQUEST_CODE_NEXT_QUESTION
import com.nzf.turbozakat.asset.Asset
import kotlinx.android.synthetic.main.asset_list_item.view.*

class McrQuestionAdapter(val asset: Asset, val question: Question?, val answers: List<Answer>) : BaseAdapter() {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.asset_list_item, parent, false)

        val mcrAnswer = getItem(position)

        if (mcrAnswer is Answer) {
            view.textView.text = mcrAnswer.answer

                if (mcrAnswer.nextQuestion != null) {
                    view.setOnClickListener {

                        asset.questions.add(question)

                        mcrAnswer.calculation.invoke(mcrAnswer.answer)

                        val intent = Intent(parent.context, AssetActivity::class.java)
                        intent.putExtra(KEY_ASSET, asset)
                        intent.putExtra(KEY_NEXT_QUESTION, mcrAnswer.nextQuestion)
                        val context = parent.context;
                        if (context is AppCompatActivity) {
                            context.startActivityForResult(intent, REQUEST_CODE_NEXT_QUESTION)
                        }
                    }
                }

        } else {
            view.setOnClickListener(null)
        }
        return view
    }

    override fun getItem(position: Int): Answer {
        return answers.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return answers.size
    }
}