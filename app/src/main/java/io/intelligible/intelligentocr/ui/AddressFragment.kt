package io.intelligible.intelligentocr.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.mlkit.nl.entityextraction.*
import io.intelligible.intelligentocr.R
import io.intelligible.intelligentocr.databinding.FragmentAddressInfoDisplayBinding


class AddressFragment : Fragment(R.layout.fragment_address_info_display) {

    private lateinit var entityExtractor: EntityExtractor
    private lateinit var binding: FragmentAddressInfoDisplayBinding
    private val infoDisplayFragmentArgs: AddressFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentAddressInfoDisplayBinding.bind(view)

        binding.rawtext.append(infoDisplayFragmentArgs.text)
        binding.Back.setOnClickListener(){
            val i = Intent(activity, MainActivity::class.java)
            startActivity(i)
        }
        entityExtractor =
            EntityExtraction.getClient(
                EntityExtractorOptions.Builder(infoDisplayFragmentArgs.language)
                    .build()
            )


        extractEntities()


    }
    private fun mapSearchAsk (address:String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity!!)
        val downloadAlertDialog: AlertDialog = builder.create()
        downloadAlertDialog.setCancelable(true)
        builder.setTitle("提醒")
            .setMessage("是否查詢${address}")
        builder.setNegativeButton("取消") { _, _ ->
            downloadAlertDialog.dismiss()
        }
        builder.setPositiveButton("確認") { _, _ ->
            val map = "http://maps.google.co.in/maps?q=$address"
            val maoIntent = Intent(Intent.ACTION_VIEW)
            maoIntent.data = Uri.parse(map)
            startActivity(maoIntent)
        }
        builder.setCancelable(true)
        builder.show()
    }

    private fun extractEntities() {
        entityExtractor
            .downloadModelIfNeeded()
            .addOnSuccessListener {


                val params =
                    EntityExtractionParams.Builder(infoDisplayFragmentArgs.text).build()
                entityExtractor
                    .annotate(params)
                    .addOnSuccessListener { entityAnnotation ->

                        categoriesEntities(entityAnnotation)
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            requireContext(),
                            "Entity Extraction failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Model Download failed", Toast.LENGTH_SHORT).show()
           }

    }

    private fun categoriesEntities(entityAnnotation: MutableList<EntityAnnotation>) {
        binding.rawtext.append("\n ======= Other Entities ======= \n")
        for (entitiy in entityAnnotation) {
            val listOfEntities = entitiy.entities
            for (entity in listOfEntities) {


                when (entity.type) {

                    Entity.TYPE_ADDRESS -> {
                        mapSearchAsk(entitiy.annotatedText)
                    }
                    else -> {
                        binding.rawtext.append(entitiy.annotatedText + "\n")
                    }
                }


            }


        }
    }
}