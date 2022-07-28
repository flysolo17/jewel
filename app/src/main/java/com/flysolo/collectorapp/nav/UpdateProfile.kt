package com.flysolo.collectorapp.nav

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.flysolo.collectorapp.ProgressDialog
import com.flysolo.collectorapp.R
import com.flysolo.collectorapp.databinding.FragmentProfileBinding
import com.flysolo.collectorapp.databinding.FragmentUpdateProfileBinding
import com.flysolo.collectorapp.models.Collector
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
private const val ARG_PARAM4 = "param4"
private const val ARG_PARAM5= "param5"
private const val ARG_PARAM6= "param6"
class UpdateProfile : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var param3: String? = null
    private var param4: String? = null
    private var param5: String? = null
    private var param6: String? = null
    private lateinit var binding : FragmentUpdateProfileBinding
    private lateinit var progressDialog : ProgressDialog
    private val firestore  = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            param3 = it.getString(ARG_PARAM3)
            param4= it.getString(ARG_PARAM4)
            param5= it.getString(ARG_PARAM5)
            param6 = it.getString(ARG_PARAM6)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUpdateProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(requireActivity())
        binding.inputFirstname.setText(param2)
        binding.inputLastname.setText(param3)
        binding.inputPhoneNumber.setText(param4)
        binding.inputPlateNumber.setText(param5)
        binding.buttonUpdateAccount.setOnClickListener {
            val firstname = binding.inputFirstname.text.toString()
            val lastName = binding.inputLastname.text.toString()
            val phone = binding.inputPhoneNumber.text.toString()
            val plateNumber = binding.inputPlateNumber.text.toString()


            if (firstname.isEmpty()) {
                binding.inputFirstname.error = "enter firstname"
            } else if (lastName.isEmpty()) {
                binding.inputLastname.error = "enter lastname"
            }  else if (phone.isEmpty()) {
                binding.inputPhoneNumber.error = "enter phone"
            } else if (phone.length != 11) {
                binding.inputPhoneNumber.error = "invalid phone number"
            } else if (plateNumber.isEmpty()) {
                binding.inputPlateNumber.error = "enter plate number"
            } else {
                val collector = Collector(
                    id = param1,
                    firstName = firstname,
                    lastName = lastName,
                    phone = phone,
                    plateNumber = plateNumber,
                    email = param6)
                updateCollectorProfile(collector)
            }
        }
    }
    private fun updateCollectorProfile(collector: Collector,) {
        progressDialog.isLoading()
        firestore.collection(Collector.TABLE_NAME)
            .document(collector.id!!)
            .set(collector)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog.stopLoading()
                    Toast.makeText(binding.root.context,"Account Added Successfully", Toast.LENGTH_SHORT).show()
                    dismiss()
                } else {
                    progressDialog.stopLoading()
                    Toast.makeText(binding.root.context,"Failed adding account info", Toast.LENGTH_SHORT).show()
                }
            }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UpdateProfile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String,param3: String,param4: String,param5: String,param6 : String) =
            UpdateProfile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, param3)
                    putString(ARG_PARAM4, param4)
                    putString(ARG_PARAM5, param5)
                    putString(ARG_PARAM6, param6)

                }
            }
    }

}