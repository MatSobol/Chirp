package com.example.chirp.fragments.auth.login

import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.chirp.databinding.AuthFragmentLoginBinding

import com.example.chirp.R
import com.example.chirp.failAlert
import com.example.chirp.successAlert
import com.example.chirp.utility.hideKeyboardFrom
import com.example.chirp.viewModel.auth.LoginViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by activityViewModel()
    private var _binding: AuthFragmentLoginBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AuthFragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailTextInputLayout = binding.email
        val passwordTextInputLayout = binding.password
        val emailEditText = emailTextInputLayout.editText
        val passwordEditText = passwordTextInputLayout.editText
        val loginButton = binding.login
        val loadingProgressBar = binding.loading
        val textNavigation = binding.loginTextNavigate


        if (emailEditText == null || passwordEditText == null) {
            throw Exception("No editText")
        }

        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                loginButton.isEnabled = loginFormState.isDataValid
                loginFormState.emailError?.let {
                    handleError(emailTextInputLayout, emailEditText, it)
                } ?: run {
                    handleNoError(emailTextInputLayout, emailEditText)
                }
                loginFormState.passwordError?.let {
                    handleError(passwordTextInputLayout, passwordEditText, it)
                } ?: run {
                    handleNoError(passwordTextInputLayout, passwordEditText)
                }
            })

        loginViewModel.loginResultAuth.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                loadingProgressBar.visibility = View.GONE
                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
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
                loginViewModel.loginDataChanged(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }
        emailEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                context?.let { it1 -> hideKeyboardFrom(it1, view) }
                loadingProgressBar.visibility = View.VISIBLE
                loginViewModel.login(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }

        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            context?.let { it1 -> hideKeyboardFrom(it1, view) }
            loginViewModel.login(
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }

        textNavigation.setOnClickListener {
            val navController = this.findNavController()
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun handleError(textInputLayout: TextInputLayout, editText: EditText, value: Int) {
        editText.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.red
            )
        )
        textInputLayout.error = getString(value)
    }

    private fun handleNoError(textInputLayout: TextInputLayout, editText: EditText) {
        val colorStateList =
            ContextCompat.getColorStateList(requireContext(), R.color.text_color_input)
        editText.setTextColor(
            colorStateList
        )
        textInputLayout.error = null
    }

    private fun updateUiWithUser() {
        val appContext = context?.applicationContext ?: return
        view?.let { successAlert(appContext, it, getString(R.string.login_successful)) }
    }

    private fun showLoginFailed(message: String) {
        val appContext = context?.applicationContext ?: return
        view?.let { failAlert(appContext, it, message) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}