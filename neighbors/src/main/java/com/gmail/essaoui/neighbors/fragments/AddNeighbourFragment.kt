package com.gmail.essaoui.neighbors.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.fragment.app.Fragment
import com.gmail.essaoui.neighbors.NavigationListener
import com.gmail.essaoui.neighbors.R
import com.gmail.essaoui.neighbors.data.NeighborRepository
import com.gmail.essaoui.neighbors.models.Neighbor
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddNeighbourFragment : Fragment(), TextWatcher {

    private lateinit var nameField: TextInputEditText
    private lateinit var emailAddressField: TextInputEditText
    private lateinit var phoneField: TextInputEditText
    private lateinit var websiteField: TextInputEditText
    private lateinit var aboutMeField: TextInputEditText
    private lateinit var imageField: TextInputEditText

    private lateinit var saveButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.add_neighbour_fragment, container, false)

        (activity as? NavigationListener)?.updateTitle(R.string.add_neighbour_toolbar_name)

        nameField = view.findViewById(R.id.name)
        emailAddressField = view.findViewById(R.id.emailAddress)
        phoneField = view.findViewById(R.id.phone)
        websiteField = view.findViewById(R.id.website)
        aboutMeField = view.findViewById(R.id.about_me)
        imageField = view.findViewById(R.id.image)
        saveButton = view.findViewById(R.id.save_button)

        saveButton.setOnClickListener {

            // On récupère la taille de la liste des voisins pour incrémenter automatiquement l'ID
            val newNeighborId = NeighborRepository.getInstance().getNeighbours().size + 1

            val name: String = nameField.text.toString()
            val address: String = emailAddressField.text.toString()
            val phone: String = phoneField.text.toString()
            val website: String = websiteField.text.toString()
            val aboutMe: String = aboutMeField.text.toString()
            val image: String = imageField.text.toString()

            val newNeighbour = Neighbor(
                id = newNeighborId.toLong(),
                name = name,
                address = address,
                phoneNumber = phone,
                webSite = website,
                aboutMe = aboutMe,
                avatarUrl = image,
                favorite = false
            )
            NeighborRepository.getInstance().createNeighbour(newNeighbour)

            // dès que le voisin est créé, on revient sur la liste des voisins
            (activity as? NavigationListener)?.showFragment(ListNeighborsFragment())
        }
        nameField.addTextChangedListener(this)
        emailAddressField.addTextChangedListener(this)
        phoneField.addTextChangedListener(this)
        websiteField.addTextChangedListener(this)
        aboutMeField.addTextChangedListener(this)
        imageField.addTextChangedListener(this)

        return view
    }

    private fun verifyButton() {
        val nameFieldNotNull: Boolean = !nameField.text.isNullOrEmpty()
        val emailNotNull: Boolean = !emailAddressField.text.isNullOrEmpty()
        val phoneNotNull: Boolean = !phoneField.text.isNullOrEmpty()
        val websiteNotNull: Boolean = !websiteField.text.isNullOrEmpty()
        val aboutMeNotNull: Boolean = !aboutMeField.text.isNullOrEmpty()
        val imageNotNull: Boolean = !imageField.text.isNullOrEmpty()

        val emailValid: Boolean = isValidEmail(emailAddressField.text.toString())
        if (!emailValid && emailNotNull) {
            emailAddressField.error = "Invalid email address"
        }

        val phoneNumberValid: Boolean = isValidPhoneNumber(phoneField.text.toString())
        if (!phoneNumberValid && phoneNotNull) {
            phoneField.error = "Format must be 0X XX XX XX XX"
        }

        val imageUrlValid: Boolean = isValidUrl(imageField.text.toString())
        if (!imageUrlValid && imageNotNull) {
            imageField.error = "Invalid image URL"
        }

        val websiteUrlValid: Boolean = isValidUrl(websiteField.text.toString())
        if (!websiteUrlValid && websiteNotNull) {
            websiteField.error = "Invalid URL"
        }

        saveButton.isEnabled =
            nameFieldNotNull &&
            emailNotNull &&
            phoneNotNull &&
            websiteNotNull &&
            aboutMeNotNull &&
            imageNotNull &&
            emailValid &&
            phoneNumberValid &&
            imageUrlValid &&
            websiteUrlValid
    }

    private fun isValidEmail(target: String): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun isValidPhoneNumber(target: String): Boolean {
        return (
            (
                (target).startsWith("07") ||
                    (target).startsWith("06")
                ) &&
                target.length == 10
            )
    }

    private fun isValidUrl(target: String): Boolean {
        return URLUtil.isValidUrl(target)
    }

    // TextWatcher

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        verifyButton()
    }
}
