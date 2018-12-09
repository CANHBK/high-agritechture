package com.example.administrator.glasshouse.ui.relay

import android.app.Activity
import android.content.Context
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.rx2.Rx2Apollo
import com.example.administrator.glasshouse.*
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.type.NodeControlInput
import com.example.administrator.glasshouse.type.StateRelayInput
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber

class RelayAdapter(val relays: List<AllRelayOfControlQuery.AllRelaysOfControl>, val context: Context, val recyclerViewBtn: androidx.recyclerview.widget.RecyclerView, val activity: Activity) : androidx.recyclerview.widget.RecyclerView.Adapter<RelayAdapter.ViewHolder>() {
    lateinit var view: View
    lateinit var st1: String
    lateinit var st2: String
    lateinit var st3: String
    lateinit var st4: String
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("!calll", "onCreateViewHolder")
        val layoutInflater = LayoutInflater.from(parent.context)
        view = layoutInflater.inflate(R.layout.item_btn_of_off, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("!calll", "getItemCount")
        return relays.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("!calll", "onBind")
        holder.progress.visibility = View.INVISIBLE
        val relay = relays[position]
        trackStateRelay(relay.serviceTag()!!, relay.nodeControl())
        holder.txtNameRelay.text = relay.name()
        val stateCurrent = relay.state()
        if (stateCurrent == "O") {
            holder.btnRelay.backgroundTintList = (context.resources.getColorStateList(R.color.colorAccent))
        }
        if (stateCurrent == "F") {
            holder.btnRelay.backgroundTintList = (context.resources.getColorStateList(R.color.secondary_text))
        }
        if (position % 2 == 0) {
            val params = holder.materialLayout.layoutParams as androidx.recyclerview.widget.GridLayoutManager.LayoutParams
            params.setMargins(16, 16, 8, 16)
        } else {
            val params = holder.materialLayout.layoutParams as androidx.recyclerview.widget.GridLayoutManager.LayoutParams
            params.setMargins(16, 16, 16, 8)
        }
        Log.d("!po", position.toString())

        if (relay.index().toInt() == 1) st1 = relay.state();
        if (relay.index().toInt() == 2) st2 = relay.state();
        if (relay.index().toInt() == 3) st3 = relay.state();
        if (relay.index().toInt() == 4) st4 = relay.state();

        holder.btnRelay.setOnClickListener {
            var s = ""

            if (relay.index().toInt() == 1) {
                s = if (st1 == "O") "F" else "O"
            }
            if (relay.index().toInt() == 2) {
                s = if (st2 == "O") "F" else "O"
            }
            if (relay.index().toInt() == 3) {
                s = if (st3 == "O") "F" else "O"
            }
            if (relay.index().toInt() == 4) {
                s = if (st4 == "O") "F" else "O"
            }


            holder.progress.visibility = View.VISIBLE
            setStateRelay(relay.serviceTag()!!, relay.nodeControl(), relay.index(), s, holder, context)


        }

        holder.btnRelay.setOnLongClickListener {

            return@setOnLongClickListener true
        }

    }

    inner class ViewHolder(val item: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(item) {

        val txtNameRelay: TextView = item.findViewById<View>(R.id.txt_name_relay) as TextView
        val btnRelay: View = item.findViewById<View>(R.id.btnRelay) as Button
        val progress: View = item.findViewById<View>(R.id.progress_circular_on_off) as ProgressBar
        val materialLayout = item.findViewById<View>(R.id.btn_material) as MaterialCardView
        fun showError(errMessage: String) {
            Snackbar.make(item, errMessage, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun setStateRelay(serviceTag: String, nodeControl: String, index: Long,
                              state: String, holder: ViewHolder, context: Context) {
        Log.d("!po","dat relay so $index voi trang thai $state ")
        val input = StateRelayInput.builder().index(index)
                .nodeControl(nodeControl).serviceTag(serviceTag).state(state).build()
        MyApolloClient.getApolloClient().mutate(
                SetStateRelayMutation.builder().params(input).build()
        ).enqueue(object : ApolloCall.Callback<SetStateRelayMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                holder.progress.visibility = View.INVISIBLE
            }

            override fun onResponse(response: Response<SetStateRelayMutation.Data>) {
                activity.runOnUiThread {
                    if (response.errors().size == 0) {
                        Log.d("!test", response.data()!!.setStateRelay()!!.toString())
                        val success = response.data()!!.setStateRelay()!!
                        if (index.toInt() == 1) {
                            st1 = state
                            if (success && state == "O") {

                                holder.btnRelay.backgroundTintList = (context.resources.getColorStateList(R.color.colorAccent))
                                holder.progress.visibility = View.INVISIBLE
                            }
                            if (success && state == "F") {
                                holder.btnRelay.backgroundTintList = (context.resources.getColorStateList(R.color.secondary_text))
                                holder.progress.visibility = View.INVISIBLE
                            }
                        }

                        if (index.toInt() == 2) {
                            st2 = state
                            if (success && state == "O") {

                                holder.btnRelay.backgroundTintList = (context.resources.getColorStateList(R.color.colorAccent))
                                holder.progress.visibility = View.INVISIBLE
                            }
                            if (success && state == "F") {
                                holder.btnRelay.backgroundTintList = (context.resources.getColorStateList(R.color.secondary_text))
                                holder.progress.visibility = View.INVISIBLE
                            }
                        }
                        if (index.toInt() == 3) {
                            st3 = state
                            if (success && state == "O") {

                                holder.btnRelay.backgroundTintList = (context.resources.getColorStateList(R.color.colorAccent))
                                holder.progress.visibility = View.INVISIBLE
                            }
                            if (success && state == "F") {
                                holder.btnRelay.backgroundTintList = (context.resources.getColorStateList(R.color.secondary_text))
                                holder.progress.visibility = View.INVISIBLE
                            }
                        }
                        if (index.toInt() == 4) {
                            st4 = state
                            if (success && state == "O") {

                                holder.btnRelay.backgroundTintList = (context.resources.getColorStateList(R.color.colorAccent))
                                holder.progress.visibility = View.INVISIBLE
                            }
                            if (success && state == "F") {
                                holder.btnRelay.backgroundTintList = (context.resources.getColorStateList(R.color.secondary_text))
                                holder.progress.visibility = View.INVISIBLE
                            }
                        }


                    } else {
                        holder.progress.visibility = View.INVISIBLE
                        val error = response.errors()[0].message()!!;
                        holder.showError(error);
                    }

                }

            }
        })
    }

    private fun trackStateRelay(serviceTag: String, nodeControl: String) {
        var compositeDisposable: CompositeDisposable? = null
        compositeDisposable = CompositeDisposable()
        val input = NodeControlInput.builder().serviceTag(serviceTag).nodeControl(nodeControl).build()
        Log.d("!input", input.toString())
        val newEnvSub = TrackStateRelaySubscription.builder().params(input).build()
        val envSubscriptionClient = MyApolloClient.getApolloClient().subscribe(newEnvSub)
        // Sử dụng RxJava để tiện xử lý sự kiện
        compositeDisposable.add(Rx2Apollo.from(envSubscriptionClient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<Response<TrackStateRelaySubscription.Data>>() {
                    override fun onComplete() {
                        //super.onComplete()
                        Log.d("!subOnCompelete", "onComplete")
                    }

                    override fun onError(t: Throwable?) {
                        //super.onError(t)
                        Log.d("!subOnError", t!!.message)
                    }

                    override fun onNext(response: Response<TrackStateRelaySubscription.Data>) {
                        allRelays(serviceTag, nodeControl)
                    }
                })
        )
    }

    private fun allRelays(serviceTag: String, nodeControl: String) {
        val input = NodeControlInput.builder().nodeControl(nodeControl)
                .serviceTag(serviceTag).build()
        MyApolloClient.getApolloClient().query(
                AllRelayOfControlQuery.builder().params(input).build()
        ).enqueue(object : ApolloCall.Callback<AllRelayOfControlQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!getControl", e.message)
            }

            override fun onResponse(response: Response<AllRelayOfControlQuery.Data>) {
                activity.runOnUiThread {
                    val data = response.data()
                    val error = response.errors()
                    if (data != null) {
                        val layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 2)
                        recyclerViewBtn.layoutManager = layoutManager
                        val adapter = RelayAdapter(data.allRelaysOfControl()!!, context, recyclerViewBtn, activity)
                        recyclerViewBtn.adapter = adapter
                    }

                    if (error.size != 0) {
                        val errorMess = error[0].message()!!;

                        Snackbar.make(view, errorMess, Snackbar.LENGTH_LONG).show()
                    }
                }

            }

        })
    }
}