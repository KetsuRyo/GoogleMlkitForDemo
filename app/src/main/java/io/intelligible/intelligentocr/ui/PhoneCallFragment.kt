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
import io.intelligible.intelligentocr.databinding.FragmentPhonecallInfoDisplayBinding


class PhoneCallFragment : Fragment(R.layout.fragment_phonecall_info_display) {

    private lateinit var entityExtractor: EntityExtractor
    lateinit var binding: FragmentPhonecallInfoDisplayBinding
    private val infoDisplayFragmentArgs: PhoneCallFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentPhonecallInfoDisplayBinding.bind(view)

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
     private fun phoneCallAsk (phoneNumber:String){
        val number = phoneNumber.replace("-", "")
        val numberClean = number.replace("(","")
        val numberClean2 = numberClean.replace(")","")
        val numberClean3 = numberClean2.replace(" ","")

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity!!)
        val downloadAlertDialog: AlertDialog = builder.create()
        downloadAlertDialog.setCancelable(true)
        builder.setTitle("提醒")
            .setMessage("是否撥打電話${numberClean3}")
        builder.setNegativeButton("取消") { _, _ ->
            downloadAlertDialog.dismiss()
        }
        builder.setPositiveButton("確認") { _, _ ->
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:$numberClean3")
            startActivity(callIntent)
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

                    Entity.TYPE_PHONE -> {
                            phoneCallAsk(entitiy.annotatedText)

                    }

                    else -> {
                        binding.rawtext.append(entitiy.annotatedText + "\n")
                    }
                }


            }


        }
    }
}