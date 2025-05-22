package com.example.chirp.fragments.auth.register

import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

import com.example.chirp.R
import com.example.chirp.databinding.AuthFragmentRegisterBinding
import com.example.chirp.failAlert
import com.example.chirp.successAlert
import com.example.chirp.utility.handleError
import com.example.chirp.utility.handleNoError
import com.example.chirp.utility.hideKeyboardFrom
import com.example.chirp.viewModel.auth.RegisterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {

    private val registerViewModel: RegisterViewModel by activityViewModel()
    private var _binding: AuthFragmentRegisterBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AuthFragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameTextInputLayout = binding.name
        val emailTextInputLayout = binding.email
        val passwordTextInputLayout = binding.password
        val repeatPasswordTextInputLayout = binding.repeatPassword
        val nameEditText = nameTextInputLayout.editText
        val emailEditText = emailTextInputLayout.editText
        val passwordEditText = passwordTextInputLayout.editText
        val repeatPasswordEditText = repeatPasswordTextInputLayout.editText

        val checkBox = binding.checkBox
        val registerButton = binding.register
        val loadingProgressBar = binding.loading
        val textNavigation = binding.loginTextNavigate

        if (nameEditText == null || emailEditText == null || passwordEditText == null || repeatPasswordEditText == null) {
            throw Exception("No editText")
        }

        registerViewModel.registerFormState.observe(viewLifecycleOwner,
            Observer { registerFormState ->
                if (registerFormState == null) {
                    return@Observer
                }
                registerButton.isEnabled = registerFormState.isDataValid
                registerFormState.nameError?.let {
                    handleError(nameTextInputLayout, nameEditText, it, requireContext())
                } ?: run {
                    handleNoError(nameTextInputLayout, nameEditText, requireContext())
                }
                registerFormState.emailError?.let {
                    handleError(emailTextInputLayout, emailEditText, it, requireContext())
                } ?: run {
                    handleNoError(emailTextInputLayout, emailEditText, requireContext())
                }
                registerFormState.passwordError?.let {
                    handleError(passwordTextInputLayout, passwordEditText, it, requireContext())
                } ?: run {
                    handleNoError(passwordTextInputLayout, passwordEditText, requireContext())
                }
                registerFormState.repeatPasswordError?.let {
                    handleError(
                        repeatPasswordTextInputLayout,
                        repeatPasswordEditText,
                        it,
                        requireContext()
                    )
                } ?: run {
                    handleNoError(
                        repeatPasswordTextInputLayout,
                        repeatPasswordEditText,
                        requireContext()
                    )
                }
            })

        registerViewModel.registerResultAuth.observe(viewLifecycleOwner,
            Observer { registerResult ->
                registerResult ?: return@Observer
                loadingProgressBar.visibility = View.GONE
                registerResult.error?.let {
                    showRegisterFailed(it)
                }
                registerResult.success?.let {
                    updateUiWithUser()
                    lifecycleScope.launch {
                        delay(1500)
                        activity?.finish()
                    }
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                registerViewModel.registerDataChanged(
                    nameEditText.text.toString(),
                    emailEditText.text.toString(),
                    passwordEditText.text.toString(),
                    repeatPasswordEditText.text.toString()
                )
            }
        }
        nameEditText.addTextChangedListener(afterTextChangedListener)
        emailEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        repeatPasswordEditText.addTextChangedListener(afterTextChangedListener)

        repeatPasswordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loadingProgressBar.visibility = View.VISIBLE
                registerViewModel.register(
                    nameEditText.text.toString(),
                    emailEditText.text.toString(),
                    passwordEditText.text.toString(),
                )
            }
            false
        }

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                repeatPasswordEditText.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            } else {
                passwordEditText.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                repeatPasswordEditText.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            }
            passwordEditText.setSelection(passwordEditText.text.length)
            repeatPasswordEditText.setSelection(repeatPasswordEditText.text.length)
        }

        registerButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            context?.let { it1 -> hideKeyboardFrom(it1, view) }
            registerViewModel.register(
                nameEditText.text.toString(),
                emailEditText.text.toString(),
                passwordEditText.text.toString(),
            )
        }

        textNavigation.setOnClickListener {
            val navController = this.findNavController()
            navController.navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun updateUiWithUser() {
        val appContext = context?.applicationContext ?: return
        view?.let { successAlert(appContext, it, getString(R.string.registration_successful)) }
    }

    private fun showRegisterFailed(message: String) {
        val appContext = context?.applicationContext ?: return
        view?.let { failAlert(appContext, it, message) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}