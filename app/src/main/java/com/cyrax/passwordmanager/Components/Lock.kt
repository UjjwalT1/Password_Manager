package com.cyrax.passwordmanager.Components

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyrax.passwordmanager.R
import com.cyrax.passwordmanager.db.Pin
import com.cyrax.passwordmanager.ui.theme.clrScheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Lock(appViewModel: AppViewModel){
    var PIN  = appViewModel.PIN.collectAsState().value
    val focusRequester = remember{ FocusRequester() }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(clrScheme.onSecondary) , horizontalAlignment = Alignment.CenterHorizontally){
        Spacer(Modifier.height(90.dp))
        Text(text = "Enter PIN :",fontSize = 30.sp , fontWeight = FontWeight.SemiBold , color = clrScheme.onPrimary)
        Spacer(Modifier.height(30.dp))

        BasicTextField(value = PIN , onValueChange = {if(it.length <= 6)appViewModel.setPin(it)} , modifier = Modifier
            .size(1.dp, 1.dp)
            .focusRequester(focusRequester), singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        Row {
            (0 .. 5).forEach {ind ->
                Block(appViewModel,try{PIN[ind].toString()}catch(_:Exception){""}, Modifier) { focusRequester.requestFocus()}
                if(ind<5)Spacer(Modifier.width(12.dp))
            }
        }
        Spacer(Modifier.height(30.dp))
        Text(text = "In case the password is forgotten reset the application" , modifier = Modifier.width(300.dp))


    }
}

@Composable
fun Block( appViewModel: AppViewModel ,key:String , modifier: Modifier , onClick:()->Unit){

    val a = animateColorAsState(targetValue =
    if(appViewModel.PIN.collectAsState().value.length == 6 && appViewModel.PIN.collectAsState().value!= appViewModel.actualPIN!!.pin) clrScheme.errorContainer
        else clrScheme.onPrimary
        , label = "")
    PinField(a.value ,key,modifier,true, onClick)

}

@Composable
fun PinField(a: Color = clrScheme.onPrimary, key:String, modifier: Modifier, star:Boolean, onClick:()->Unit){
    Column(modifier = Modifier
        .size(45.dp, 50.dp)
        .clip(RoundedCornerShape(12.dp))
        .clickable { onClick() }
        .background(a)
        .padding(start = 0.dp) , verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = if(key=="")"" else if(star)"*" else key ,
            fontSize = 25.sp , fontWeight = FontWeight.Bold , color = clrScheme.onSecondary,
            modifier = modifier.wrapContentSize(Alignment.Center)
        )
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SetLock(appViewModel: AppViewModel , close:()->Unit){
    val ctxt = LocalContext.current
    var PIN2  by remember{ mutableStateOf("") }
    var PIN3  by remember{ mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var stage by remember{ mutableStateOf(if(appViewModel.actualPIN != null)0 else 2) }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(clrScheme.onSecondary) , horizontalAlignment = Alignment.CenterHorizontally){
        Spacer(Modifier.height(10.dp))
        Row{
            Spacer(Modifier.width(15.dp))
            IconButton(onClick = { close() }) {
                Image(Icons.Rounded.Close, contentDescription = null , Modifier.size(40.dp), colorFilter = ColorFilter.tint(
                    clrScheme.background) )        }
            Spacer(Modifier.weight(1f))
        }
        Spacer(Modifier.height(80.dp))
        when(stage){
            0 -> {
                var PIN1  by remember{ mutableStateOf("") }
                val focusRequester = remember{ FocusRequester() }
                val a = animateColorAsState(targetValue =
                if(PIN1.length == 6 &&  PIN1 != appViewModel.actualPIN!!.pin) clrScheme.errorContainer
                else clrScheme.onPrimary
                    , label = "")
                if(PIN1.length == 6 &&  PIN1 == appViewModel.actualPIN!!.pin)  stage  =1 ;
                Text(
                    text = "Verify PIN :",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = clrScheme.onPrimary
                )
                Spacer(Modifier.height(30.dp))
                BasicTextField(
                    value = PIN1, onValueChange = { if (it.length <= 6) PIN1 = it },
                    modifier = Modifier
                        .size(1.dp, 1.dp)
                        .focusRequester(focusRequester),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                Row {
                    (0..5).forEach { ind ->
                        PinField(a.value,
                            key = try {
                                PIN1[ind].toString()
                            } catch (_: Exception) {
                                ""
                            }, modifier = Modifier, star = true
                        ) { focusRequester.requestFocus() }
                        if (ind < 5) Spacer(Modifier.width(12.dp))
                    }
                }
                Spacer(Modifier.height(30.dp))
            }
            1->{
                Row( modifier = Modifier
                    .fillMaxWidth(.6f)
                    .height(50.dp)
                    .background(clrScheme.onBackground, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        appViewModel.removeActualPin(ctxt)
                        Toast
                            .makeText(ctxt, "PIN Removed", Toast.LENGTH_SHORT)
                            .show()
                        close()
                    },verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Center ){
                    Spacer(Modifier.width(7.dp))
                    Image(painterResource(R.drawable.delete) , null, modifier = Modifier.size(40.dp))
                    Spacer(Modifier.width(30.dp))
                    Text(text = "Remove PIN" , color = clrScheme.onPrimary , fontSize = 20.sp , fontWeight = FontWeight.Black)
                    Spacer(Modifier.width(40.dp))

                }
                Spacer(Modifier.height(10.dp))
                Row( modifier = Modifier
                    .fillMaxWidth(.6f)
                    .height(50.dp)
                    .background(clrScheme.onBackground, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        stage = 2;
                    },verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Center ){
                    Spacer(Modifier.width(3.dp))
                    Image(painterResource(R.drawable.loop) , null, modifier = Modifier.size(35.dp))
                    Spacer(Modifier.width(30.dp))
                    Text(text = "Update PIN" , color = clrScheme.onPrimary , fontSize = 20.sp , fontWeight = FontWeight.Black)
                    Spacer(Modifier.width(40.dp))
                }
            }
            2 ->{

                val focusRequester2 = remember{ FocusRequester() }
                Text(
                    text = "Enter New PIN :",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = clrScheme.onPrimary
                )
                Spacer(Modifier.height(30.dp))
                BasicTextField(value = PIN2 , onValueChange = {if(it.length <= 6)PIN2 = it} , modifier = Modifier
                    .size(1.dp, 1.dp)
                    .focusRequester(focusRequester2), singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                Row {
                    (0 .. 5).forEach {ind ->
                        PinField(key = try{PIN2[ind].toString()}catch(_:Exception){""}, modifier =  Modifier,star = false) { focusRequester2.requestFocus()}
                        if(ind<5)Spacer(Modifier.width(12.dp))
                    }
                }
                if(PIN2.length == 6)  stage  = 3 ;
            }
            3 ->{
                val a = animateColorAsState(targetValue =
                if(PIN3.length == 6 &&  PIN3 != PIN2) {
                    clrScheme.errorContainer
                }
                else clrScheme.onPrimary
                    , label = "")
                val focusRequester2 = remember{ FocusRequester() }
                Text(
                    text = "Confirm New PIN :",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = clrScheme.onPrimary
                )
                Spacer(Modifier.height(30.dp))
                BasicTextField(value = PIN3 , onValueChange = {if(it.length <= 6)PIN3 = it} , modifier = Modifier
                    .size(1.dp, 1.dp)
                    .focusRequester(focusRequester2), singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                Row {
                    (0 .. 5).forEach {ind ->
                        PinField(a.value,key = try{PIN3[ind].toString()}catch(_:Exception){""}, modifier =  Modifier,star = false) { focusRequester2.requestFocus()}
                        if(ind<5)Spacer(Modifier.width(12.dp))
                    }
                }
                if(PIN3.length == 6 &&  PIN3 == PIN2) {
                    LaunchedEffect(key1 = Unit){
                        if(appViewModel.actualPIN != null) {
                            appViewModel.updateActualPin(
                                ctxt,
                                Pin(id = appViewModel.actualPIN!!.id, pin = PIN3)
                            )
                            Toast.makeText(ctxt , "Pin Updated" , Toast.LENGTH_SHORT).show()
                            close()
                        }
                        else {
                            appViewModel.setActualPin(ctxt, Pin(pin = PIN3))
                            Toast.makeText(ctxt , "Pin Set" , Toast.LENGTH_SHORT).show()
                            close()
                        }
                    }



                }
            }

        }





    }
}