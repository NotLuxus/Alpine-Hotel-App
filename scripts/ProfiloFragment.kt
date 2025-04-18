package com.example.hotel_03.fragments

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.hotel_03.R
import com.example.hotel_03.activity.MainActivity
import com.example.hotel_03.application.MyApplication
import com.example.hotel_03.databinding.FragmentProfiloBinding
import com.example.hotel_03.databinding.PopupPasswordBinding
import com.example.hotel_03.databinding.PopupSimpleBinding
import com.example.hotel_03.databinding.PopupStandardBinding
import com.example.hotel_03.viewmodels.ProfiloViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProfiloFragment : Fragment(R.layout.fragment_profilo) {

    private lateinit var binding: FragmentProfiloBinding

    private val viewModel: ProfiloViewModel by activityViewModels {
        ProfiloViewModel.factory(
            (requireContext().applicationContext as MyApplication).sharedPreferences
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfiloBinding.inflate(inflater, container, false)


        setupListeners()
        setupObservers()

        return binding.root
    }

    //Sincronizza il contenuto delle TextView del Fragment con
    //i dati contenuti nel ProfiloViewModel
    @SuppressLint("SetTextI18n")
    private fun setupObservers() {
        viewModel.nomeCognome.observe(viewLifecycleOwner) { nomeCognome ->
            binding.nomeCognomeUtenteTxt.text = nomeCognome
        }

        viewModel.email.observe(viewLifecycleOwner) { email ->
            binding.emailTxt.text = "E-mail: $email"
        }

        viewModel.dataNascita.observe(viewLifecycleOwner) { dataNascita ->
            Log.d("ProfiloFragment", "dataNascita osservata: $dataNascita")
            val formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val data = LocalDate.parse(dataNascita, formatter1)
            val formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            binding.dataNascitaTxt.text = "Data di nascita: ${data.format(formatter2)}"
        }

        viewModel.numeroTelefono.observe(viewLifecycleOwner) { numeroTelefono ->
            binding.numTelefonoTxt.text = "Numero di telefono: $numeroTelefono"
        }
    }

    //Configura i listeners dei Button del Fragment
    private fun setupListeners() {
        val navController = findNavController()

        binding.modificaDatiBtn.setOnClickListener {
            navController.navigate(R.id.action_profiloFragment_to_modificaDatiFragment)
        }

        binding.modificaPasswordBtn.setOnClickListener {
            navController.navigate(R.id.action_profiloFragment_to_modificaPasswordFragment)
        }

        binding.metodoPagamentoBtn.setOnClickListener {
            navController.navigate(R.id.action_profiloFragment_to_metodoPagamentoFragment)
        }

        binding.logoutBtn.setOnClickListener {
            showPopupLogOut()
        }

        binding.cancellaAccountBtn.setOnClickListener {
            showPopupCancellaAccount()
        }
    }

    private fun showPopupLogOut() {
        val popupBinding = PopupStandardBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(popupBinding.root)
        val popup = builder.create()
        setupPopUpListener(popupBinding, popup)
        popup.show()

        //Rimuove i margini dell'AlertDialog
        popup.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Imposta la larghezza del popup per coprire tutta la larghezza della finestra
        popup.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupPopUpListener(popupBinding: PopupStandardBinding, popup: AlertDialog) {
        popupBinding.annullaBtn.setOnClickListener(){
            popup.dismiss()
        }

        popupBinding.confermaBtn.setOnClickListener(){
            viewModel.removeUserLocalData()
            endUserSession()
        }

        popupBinding.titoloTxt.text = getString(R.string.LOGOUT)
        popupBinding.messaggioTxt.text = getString(R.string.msg_logout)
    }


    //Mostra a schermo il pop-up di conferma per effettuare la cancellazione dell'account
    private fun showPopupCancellaAccount() {
        val popupBinding = PopupPasswordBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(popupBinding.root)
        val popup = builder.create()

        setupPasswordPopupListeners(popupBinding, popup)
        bindPasswordPopup(popupBinding)
        popup.show()

        //Rimuove i margini dell'AlertDialog
        popup.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Imposta la larghezza del popup per coprire tutta la larghezza della finestra
        popup.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    //Configura i listeners dei Button del Popup
    private fun setupPasswordPopupListeners(popupBinding: PopupPasswordBinding, popup: AlertDialog) {
        popupBinding.confermaBtn.setOnClickListener {
            val password = popupBinding.passwordEdit.text.toString()
            deleteAccount(password)
            popup.dismiss()
        }

        popupBinding.annullaBtn.setOnClickListener {
            popup.dismiss()
        }
    }

    //Riempie il contenuto del Popup
    private fun bindPasswordPopup(popupBinding: PopupPasswordBinding) {
        popupBinding.titoloTxt.text = getString(R.string.title_attenzione)
        popupBinding.icon.setImageResource(R.drawable.ic_critic)
        popupBinding.messaggioTxt.text = getString(R.string.msg_cancellazione_account)
    }

    //Cancella l'account dell'utente
    private fun deleteAccount(password: String) {
        viewModel.deleteUserAccount(password).observe(viewLifecycleOwner) { isUserAccountDeleted ->
            if(isUserAccountDeleted) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.msg_account_cancellato),
                    Toast.LENGTH_SHORT
                ).show()

                endUserSession()
            }
            else {
                Toast.makeText(
                    context, getString(R.string.msg_errore), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    //Naviga nel MainActivity
    private fun endUserSession() {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

}