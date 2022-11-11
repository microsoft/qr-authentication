/*
 *
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 *
 */

package com.microsoft.samples.qrauthentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.microsoft.identity.client.*
import com.microsoft.identity.client.IPublicClientApplication.ISingleAccountApplicationCreatedListener
import com.microsoft.identity.client.ISingleAccountPublicClientApplication.CurrentAccountCallback
import com.microsoft.identity.client.ISingleAccountPublicClientApplication.SignOutCallback
import com.microsoft.identity.client.exception.MsalException


class MainActivity : AppCompatActivity() {
    private val SCOPES = arrayOf("Files.Read")

    private lateinit var signInButton: Button
    private lateinit var signOutButton: Button
    private lateinit var statusText: TextView

    private var singleAccountApp: ISingleAccountPublicClientApplication? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        signInButton = findViewById(R.id.sign_in_button)
        signOutButton = findViewById(R.id.sign_out_button)
        statusText = findViewById(R.id.status_text)

        signInButton.setOnClickListener {
            qrCodeScannerActivityResultLauncher.launch(Intent(this, QrCodeScannerActivity::class.java))
        }
        signOutButton.setOnClickListener {
            singleAccountApp?.signOut(object : SignOutCallback {
                override fun onSignOut() {
                    updateUI(null)
                }

                override fun onError(exception: MsalException) {
                    displayError(exception)
                }
            })
        }

        PublicClientApplication.createSingleAccountPublicClientApplication(
            applicationContext,
            R.raw.auth_config_single_account, object : ISingleAccountApplicationCreatedListener {
                override fun onCreated(application: ISingleAccountPublicClientApplication) {
                    singleAccountApp = application
                    loadAccount()
                }

                override fun onError(exception: MsalException) {
                    displayError(exception)
                }
            })
    }

    private val qrCodeScannerActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            // Log in with the provided UPN
            val upn = it.data?.getStringExtra(QrCodeScannerActivity.USER_UPN_INTENT)
            singleAccountApp?.signIn(this@MainActivity, upn, SCOPES, authInteractiveCallback)
        }
    }

    // When app comes to the foreground, load existing account to determine if user is signed in
    private fun loadAccount() = singleAccountApp?.run {
        getCurrentAccountAsync(object : CurrentAccountCallback {
            override fun onAccountLoaded(activeAccount: IAccount?) {
                updateUI(activeAccount)
            }

            override fun onAccountChanged(priorAccount: IAccount?, currentAccount: IAccount?) {
                updateUI(currentAccount)
            }

            override fun onError(exception: MsalException) {
                displayError(exception)
            }
        })
    }

    private fun updateUI(account: IAccount? = null) {
        account?.let {
            updateStatus("Signed in as ${it.username}")
        } ?: run {
            updateStatus("Currently signed out")
        }

        signInButton.isEnabled = account == null
        signOutButton.isEnabled = !signInButton.isEnabled
    }

    private fun displayError(exception: Exception) {
        updateStatus(exception.toString())
    }

    private fun updateStatus(status: String) {
        statusText.text = status
    }

    private val authInteractiveCallback = object : AuthenticationCallback {
        override fun onSuccess(authenticationResult: IAuthenticationResult) {
            /* Successfully got a token, use it to call a protected resource - MSGraph */
            updateUI(authenticationResult.account)
        }

        override fun onError(exception: MsalException) {
            /* Failed to acquireToken */
            Log.d(TAG, "Authentication failed: $exception")
            displayError(exception)
        }

        override fun onCancel() {
            /* User canceled the authentication */
            Log.d(TAG, "User cancelled login.")
        }
    }

    override fun onResume() {
        super.onResume()
        loadAccount()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}