package updated.mysterium.vpn.ui.payment.method

import android.content.Intent
import android.os.Bundle
import network.mysterium.vpn.R
import network.mysterium.vpn.databinding.ActivityPaymentMethodBinding
import org.koin.android.ext.android.inject
import updated.mysterium.vpn.ui.base.BaseActivity
import updated.mysterium.vpn.ui.top.up.card.currency.CardCurrencyActivity
import updated.mysterium.vpn.ui.top.up.coingate.crypto.TopUpCryptoActivity
import updated.mysterium.vpn.ui.wallet.ExchangeRateViewModel

class PaymentMethodActivity : BaseActivity() {

    companion object {
        const val REGISTRATION_MODE_EXTRA_KEY = "REGISTRATION_MODE_EXTRA_KEY"
        const val CRYPTO_AMOUNT_EXTRA_KEY = "CRYPTO_AMOUNT_EXTRA_KEY"
    }

    private lateinit var binding: ActivityPaymentMethodBinding
    private val exchangeRateViewModel: ExchangeRateViewModel by inject()
    private var cryptoAmount: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentMethodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bind()
        getCryptoValue()
    }

    private fun bind() {
        binding.cryptoPayment.setOnClickListener {
            navigateToCryptoPaymentFlow()
        }
        binding.creditCardPayment.setOnClickListener {
            navigateToCardPaymentFlow()
        }
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun getCryptoValue() {
        cryptoAmount = intent.extras?.getInt(TopUpCryptoActivity.CRYPTO_AMOUNT_EXTRA_KEY)
        cryptoAmount?.let {
            binding.usdEquivalentTextView.text = getString(
                R.string.top_up_usd_equivalent, exchangeRateViewModel.usdEquivalent * it
            )
        }
    }

    private fun navigateToCryptoPaymentFlow() {
        val intent = Intent(this, TopUpCryptoActivity::class.java).apply {
            putExtra(TopUpCryptoActivity.CRYPTO_AMOUNT_EXTRA_KEY, cryptoAmount)
            if (intent.extras?.getBoolean(REGISTRATION_MODE_EXTRA_KEY) == true) {
                putExtra(TopUpCryptoActivity.REGISTRATION_MODE_EXTRA_KEY, true)
            }
        }
        startActivity(intent)
    }

    private fun navigateToCardPaymentFlow() {
        val intent = Intent(this, CardCurrencyActivity::class.java).apply {
            putExtra(CardCurrencyActivity.CRYPTO_AMOUNT_EXTRA_KEY, cryptoAmount)
        }
        startActivity(intent)
    }
}
