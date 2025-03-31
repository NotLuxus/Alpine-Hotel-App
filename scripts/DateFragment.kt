package com.example.hotel_03.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hotel_03.R
import com.example.hotel_03.databinding.FragmentDateBinding
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.hotel_03.application.MyApplication
import com.example.hotel_03.databinding.PopupSimpleBinding
import com.example.hotel_03.viewmodels.PrenotazioniViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit


class DateFragment : Fragment(R.layout.fragment_date) {

    private val dateViewModel: PrenotazioniViewModel by activityViewModels<PrenotazioniViewModel> {
        PrenotazioniViewModel.factory(
            (requireContext().applicationContext as MyApplication).sharedPreferences
        )
    }

    private lateinit var binding: FragmentDateBinding
    private var isCheckInSelected = true
    private var checkInDate: Calendar? = null
    private var checkOutDate: Calendar? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDateBinding.inflate(inflater, container, false)

        setupListener()
        return binding.root
    }

    private fun setupListener() {
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            val today = Calendar.getInstance()

            // Controllo per date passate
            if (selectedDate.before(today)) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.msg_date_in_past),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnDateChangeListener
            }
            //controllo sull'inserimento delle date nel caledario
            if (isCheckInSelected) {
                checkInDate = selectedDate
                binding.checkInDataText.text = formatDate(selectedDate)
                binding.checkInDataText.visibility = View.VISIBLE
                isCheckInSelected = false
            } else {
                if (checkInDate == null || selectedDate.before(checkInDate)) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.msg_invalid_checkout_date),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    checkOutDate = selectedDate
                    binding.checkOutDataText.text = formatDate(selectedDate)
                    binding.checkOutDataText.visibility = View.VISIBLE
                    isCheckInSelected = true
                    binding.confermaDateButton.visibility = View.VISIBLE
                }
            }
        }
        binding.confermaDateButton.setOnClickListener {
            cercaCamere()
        }

    }
    //Cerca le camere disponibili nei giorni selezionati
    private fun cercaCamere(){
        if (checkInDate != null && checkOutDate != null) {

            // Conversione e formattazione delle date
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val checkInFormatted = formatter.format(checkInDate!!.time)
            val checkOutFormatted = formatter.format(checkOutDate!!.time)

            // Passa le date al ViewModel
            dateViewModel.cercaCamereDisponibili(checkInFormatted, checkOutFormatted).observe(viewLifecycleOwner){
                Log.d("LoginState", "Received state: $it")
                when(it) {
                    0 -> {
                        dateViewModel.numGiorni = calcolaNumGiorni(checkInDate,checkOutDate)//Salva il numero dei giorni nel view model
                        findNavController().navigate(R.id.action_dateFragment_to_camereFragment)
                    }
                    1 -> {
                        showPopupSoldOut()
                    }
                    2 -> {
                        Toast.makeText(
                            context, getString(R.string.msg_errore), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.msg_select_dates),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun formatDate(calendar: Calendar): String {
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1 // I mesi sono indicizzati da 0
        val year = calendar.get(Calendar.YEAR)
        return "$day/$month/$year"
    }

    //calcola il numero di giorni
    private fun calcolaNumGiorni(checkInDate: Calendar?, checkOutDate: Calendar?): Int {
        if (checkInDate == null || checkOutDate == null) {
            return 0
        }else{
            val checkInMillis = checkInDate.timeInMillis
            val checkOutMillis = checkOutDate.timeInMillis
            val diffInMillis = checkOutMillis - checkInMillis

            return TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()
        }

    }

    //Mostra a schermo il pop-up che avvisa che non ci sono camere disponibili
    private fun showPopupSoldOut() {
        val popupBinding = PopupSimpleBinding.inflate(layoutInflater)
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

    private fun setupPopUpListener(popupBinding: PopupSimpleBinding, popup: AlertDialog) {
        popupBinding.okBtn.setOnClickListener{
            removeDate()
            popup.dismiss()
        }

        popupBinding.titoloTxt.text = getString(R.string.msg_hotel_pieno_title)
        popupBinding.messaggioTxt.text = getString(R.string.msg_hotel_pieno)
    }

    //rimuove le date dalla UI
    @SuppressLint("SetTextI18n")
    private fun removeDate() {
        checkInDate = null
        checkOutDate = null
        isCheckInSelected = true

        binding.checkInDataText.text = ""
        binding.checkInDataText.visibility = View.GONE
        binding.checkOutDataText.text = ""
        binding.checkOutDataText.visibility = View.GONE
        binding.confermaDateButton.visibility = View.GONE
    }


}