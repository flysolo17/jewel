package com.flysolo.collectorapp.nav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.flysolo.collectorapp.R
import com.flysolo.collectorapp.adapters.UsersAdapter
import com.flysolo.collectorapp.databinding.FragmentViewHomeOwnersBinding
import com.flysolo.collectorapp.models.User
import com.flysolo.collectorapp.viewmodel.AdressViewModel
import com.google.firebase.firestore.FirebaseFirestore


class ViewHomeOwners : DialogFragment() {


    private var binding : FragmentViewHomeOwnersBinding? = null
    private lateinit var listUsers : MutableList<User>
    private lateinit var usersAdapter: UsersAdapter
    private val firestore: FirebaseFirestore  = FirebaseFirestore.getInstance()
    private lateinit var addressViewModel: AdressViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentViewHomeOwnersBinding.inflate(layoutInflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addressViewModel = ViewModelProvider(requireActivity()).get(AdressViewModel::class.java)
        binding!!.recyclerviewUsers.apply {
            this.layoutManager = LinearLayoutManager(view.context)
        }
        addressViewModel.getAddress().observe(viewLifecycleOwner) { address ->
            getAllUsers(address)
        }

    }
    private fun getAllUsers(address : String) {
        listUsers = mutableListOf()
        firestore.collection(User.TABLE_NAME)
            .addSnapshotListener { value, error ->
                listUsers.clear()
                if (error != null) {
                    error.printStackTrace()
                }
                else {
                    value?.map { document->
                        if (document != null) {
                            val  user = document.toObject(User::class.java)
                            if (user.userAddress!!.contains(address)) {
                                listUsers.add(user)
                            }
                        }
                    }
                    usersAdapter = UsersAdapter(view?.context ?: binding!!.root.context,listUsers)
                    binding!!.recyclerviewUsers.adapter = usersAdapter
                }
            }
    }


}