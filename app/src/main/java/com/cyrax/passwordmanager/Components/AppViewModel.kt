package com.cyrax.passwordmanager.Components

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrax.passwordmanager.db.Accounts
import com.cyrax.passwordmanager.db.Cards
import com.cyrax.passwordmanager.db.Pin
import com.cyrax.passwordmanager.db.db
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel:ViewModel() {
    private var inputAccount = MutableStateFlow(Accounts())
    val _inputAccount = inputAccount as StateFlow<Accounts>

    private var inputCard = MutableStateFlow(Cards())
    val _inputCard = inputCard as StateFlow<Cards>

    fun updateInp(query:Int , value:String){
        inputAccount.update {
            when(query){
                1-> it.copy(title = value)
                2-> it.copy(userName = value)
                3-> it.copy(email = value)
                4 -> it.copy(pass = value)
                else -> it.copy(title = "", userName = "", email = "", pass = "")
            }
        }
    }

    fun updateInpCard(query:Int , value:String){
        inputCard.update {
            when(query){
                1-> it.copy(title = value)
                2-> it.copy(cardNumber = value)
                3-> it.copy(validity = value)
                4 -> it.copy(cvv = value)
                else -> it.copy(title = "", cardNumber = "", validity = "", cvv = "")
            }
        }
    }

    val accounts = mutableStateListOf<Accounts>()
    val cards = mutableStateListOf<Cards>()

    fun getAccounts(context: Context){
        val db:db = db.getDatabase(context)
        viewModelScope.launch(Dispatchers.IO){
            accounts.clear()
            accounts.addAll( db.acc().getAllAcc() )
        }
    }
    fun getCards(context: Context){
        val db:db = db.getDatabase(context)
        viewModelScope.launch(Dispatchers.IO){
            cards.clear()
            cards.addAll( db.card().getAllCards() )
        }
    }

    fun addAccount(context: Context){
        val db = db.getDatabase(context)
        viewModelScope.launch(Dispatchers.IO){
            db.acc().insertAcc(_inputAccount.value)
            getAccounts(context)
            updateInp(99,"")
        }
    }
    fun addCards(context: Context){
        val db = db.getDatabase(context)
        viewModelScope.launch(Dispatchers.IO){
            db.card().insertCard(_inputCard.value)
            getCards(context)
            updateInpCard(99,"")
        }
    }

    fun deleteAccounts(context: Context , id:Long){
        val db:db = db.getDatabase(context)
        viewModelScope.launch(Dispatchers.IO){
            db.acc().removeAcc(id)
            getAccounts(context)
        }
    }
    fun deleteCards(context: Context, id:Long){
        val db:db = db.getDatabase(context)
        viewModelScope.launch(Dispatchers.IO){
            db.card().removeCard(id)
            getCards(context)
        }
    }

    fun updateAccount(context: Context,data:Accounts){
        val db = db.getDatabase(context)
        viewModelScope.launch(Dispatchers.IO){
            db.acc().updateAcc(data)
            getAccounts(context)
        }
    }
    fun updateCards(context: Context,data:Cards){
        val db = db.getDatabase(context)
        viewModelScope.launch(Dispatchers.IO){
            db.card().updateCard(data)
            getCards(context)
        }
    }

    var reFillAccount = MutableStateFlow(Accounts())
    var reFillCard = MutableStateFlow(Cards())

    fun updateAccStore(query:Int , value:String , data:Accounts){
        reFillAccount.update {
            when(query){
                1-> it.copy(title = value)
                2-> it.copy(userName = value)
                3-> it.copy(email = value)
                4 -> it.copy(pass = value)
                99 ->  it.copy(title = "", userName = "", email = "", pass = "")
                else -> it.copy(id = data.id ,title = data.title , lastUpdated = data.lastUpdated , userName = data.userName, email = data.email, pass = data.pass)
            }
        }
    }

    fun updateCardStore(query:Int , value:String , data: Cards){
        reFillCard.update {
            when(query){
                1-> it.copy(title = value)
                2-> it.copy(cardNumber = value)
                3-> it.copy(validity = value)
                4 -> it.copy(cvv = value)
                99 -> it.copy(title = "", cardNumber = "", validity = "", cvv = "")
                else -> it.copy(id = data.id ,title = data.title , lastUpdated = data.lastUpdated , cardNumber = data. cardNumber , validity = data. validity , cvv = data.cvv)
            }
        }
    }
    val wait = MutableStateFlow(true)
    val PIN = MutableStateFlow("")
    fun setPin(str:String){
        PIN.update {
            str
        }
    }

    var actualPIN:Pin? =  null
    fun getActualPin(context: Context){
        val db = db.getDatabase(context)
        viewModelScope.launch(Dispatchers.IO){
            actualPIN = db.pin().getPin()
            wait.update { false }
        }
    }
    fun setActualPin(context: Context , pin:Pin){
        val db = db.getDatabase(context)
        viewModelScope.launch(Dispatchers.IO){
            db.pin().insertPin(pin)
            getActualPin(context)
        }
    }
    fun updateActualPin(context: Context , pin:Pin){
        val db = db.getDatabase(context)
        viewModelScope.launch(Dispatchers.IO){
            db.pin().updatePin(pin)
            getActualPin(context)
        }
    }
    fun removeActualPin(context: Context){
        val db = db.getDatabase(context)
        viewModelScope.launch(Dispatchers.IO){
            actualPIN = null
            db.pin().removePin()
        }
    }

}