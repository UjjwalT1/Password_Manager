package com.cyrax.passwordmanager.Components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.PopupProperties
import com.cyrax.passwordmanager.R
import com.cyrax.passwordmanager.db.Accounts
import com.cyrax.passwordmanager.ui.theme.clrScheme
import java.text.SimpleDateFormat
import java.util.Date

fun tsToDate(timestamp: Long, format: String = "dd.MM.yyyy"): String {
    val date = Date(timestamp )
    val sdf = SimpleDateFormat(format)
    return sdf.format(date)
}



@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DispText(data:String,passView:Boolean,itIs:String){
    val context  = LocalContext.current
    var notShow by remember{ mutableStateOf(true) }
    Column(
    ){
        Row{
            Spacer(Modifier.width(4.dp))
            Text(text = itIs , fontSize = 10.sp , color = clrScheme.onPrimary)
            Spacer(Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(3.dp))
        Box(
            Modifier

                .background(clrScheme.surface, shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .clickable { notShow = !notShow }
                .padding(8.dp)

        ){
            Row(verticalAlignment = Alignment.CenterVertically){
                AnimatedContent(targetState = passView && notShow, label = "") {
                    if(it) Text(text = "********", modifier = Modifier , color = clrScheme.onPrimary )
                    else Text(text =  data, modifier = Modifier , color = clrScheme.onPrimary )
                }

                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /*TODO COPY TO CLIPBOARD*/
                                     val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clipData = ClipData.newPlainText("Text", data)
                    clipboardManager.setPrimaryClip(clipData)
                    Toast.makeText(context, "Copied !", Toast.LENGTH_SHORT).show()
                                     } , modifier =  Modifier.size(24.dp)) {
                    Image(painterResource(id = R.drawable.copy), contentDescription = null , Modifier,
                        colorFilter = ColorFilter.tint(clrScheme.onPrimary  ))
                }
                Spacer(modifier = Modifier.width(3.dp))

            }
        }
    }


}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FillText(data:String, itIs: String, keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), onValueChange:(String)->Unit  ){

    val context = LocalContext.current
    Column(
    ){
        Row{
            Spacer(Modifier.width(4.dp))
            Text(text = itIs , fontSize = 11.sp , color = clrScheme.onPrimary)
            Spacer(Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(3.dp))
        Box(
            Modifier
                .background(clrScheme.surface, shape = RoundedCornerShape(30.dp))
                .clip(RoundedCornerShape(10.dp))
                .padding(10.dp, 8.dp)

        ){
            Row(verticalAlignment = Alignment.CenterVertically){
                Box(modifier = Modifier){
                    if(data.isBlank()){
                        Text("Enter ${itIs.substring(0,itIs.length-1)}" , fontSize = 16.sp , color = Color.Gray)
                    }
                    BasicTextField(value = data , onValueChange = onValueChange , modifier = Modifier
                        .fillMaxWidth(),
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 18.sp ,
                            color = clrScheme.onPrimary
                        ),
                        keyboardOptions = keyboardOptions,

                    )
                }

                Spacer(modifier = Modifier.weight(1f))

            }
        }
    }


}






@Composable
fun Entry(data:Accounts,onClick:(Long)->Unit,currId:Long , onMoreVert:()->Unit,DDMenu:@Composable()(()->Unit)){
    Column(modifier = Modifier
        .shadow(3.dp, RoundedCornerShape(20.dp))
        //.animateContentSize()
        .background(clrScheme.onBackground, shape = RoundedCornerShape(20.dp))

    ){
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier

            .height(60.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick(data.id) }

        ){
            Spacer(modifier = Modifier.width(14.dp))
            Box(modifier = Modifier
                .size(40.dp)
                .clip(
                    RoundedCornerShape(50)
                ).background(clrScheme.background , RoundedCornerShape(50)).wrapContentSize(
                    Alignment.Center)){
                Text(text = data.title.uppercase()[0].toString() , fontSize = 30.sp,fontWeight = FontWeight.Bold,color = clrScheme.onSecondary)
            }

            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(text = data.title , fontWeight = FontWeight.Bold,color = clrScheme.onPrimary)
                Spacer(modifier = Modifier.height(3.dp))
                Text(text = "Last Updated: ${tsToDate(data.lastUpdated)}",fontWeight = FontWeight.Light , color = Color.Gray, fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.weight(1f))

            Box(modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopEnd)){
                IconButton(onClick = onMoreVert) {
                    Image( Icons.Rounded.MoreVert, contentDescription = null,Modifier.size(30.dp), colorFilter = ColorFilter.tint(
                        clrScheme.background))
                    DDMenu()
                }

            }

        }
        AnimatedVisibility(visible = data.id == currId ){
            Spacer(Modifier.height(3.dp))
            Column(modifier = Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally){
                DispText(data = data.userName, passView = false,"UserID :")
                Spacer(Modifier.height(8.dp))
                DispText(data = data.email, passView = false,"Email or Phone :")
                Spacer(Modifier.height(8.dp))
                DispText(data = data.pass, passView = true , "Password :")
                Spacer(Modifier.height(20.dp))
            }
        }


    }

}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun All(appViewModel: AppViewModel , onHamClick:()->Unit){
    val data = appViewModel._inputAccount.collectAsState().value
    val dataCard = appViewModel._inputCard.collectAsState().value
    val context = LocalContext.current
    var curr = rememberSaveable{ mutableStateOf(-1L) }
    var inp by rememberSaveable { mutableStateOf("") }
    var isDiag by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(modifier = Modifier){
                        Spacer(modifier = Modifier.weight(1f))
                        Box{
                            if(inp.isBlank()){
                                Text("Search Title or Email" , fontSize = 16.sp , color = Color.Gray)
                            }
                            BasicTextField(value = inp , onValueChange = { inp = it } , modifier = Modifier
                                .fillMaxWidth(),
                                singleLine = true,
                                textStyle = TextStyle(
                                    fontSize = 18.sp ,
                                    color = clrScheme.onPrimary
                                )

                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }

                },
                navigationIcon = {
                    IconButton(onClick = onHamClick) {
                        Image( Icons.Rounded.Menu, contentDescription = null,Modifier.size(30.dp), colorFilter = ColorFilter.tint(
                            clrScheme.background))
                    }
                },
                modifier = Modifier
                    .background(clrScheme.onSecondary)
                    .padding(14.dp, 10.dp)
                    .height(47.dp)
                    .clip(RoundedCornerShape(50.dp)),
                )


        },
        floatingActionButton = {
            IconButton(onClick = { isDiag = true },modifier = Modifier
                .size(50.dp)
                .background(clrScheme.onPrimary, RoundedCornerShape(15.dp))) {
                Image( Icons.Rounded.Add, contentDescription = null,Modifier.size(30.dp), colorFilter = ColorFilter.tint(
                    clrScheme.onSecondary))
            }
        },

    ){
        Surface(modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .background(clrScheme.onSecondary)){
            AnimatedContent(targetState = appViewModel, label = "") {appViewModel ->

                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .background(clrScheme.onSecondary)
                    .padding(20.dp, 10.dp)){
                    item {
                        Spacer(modifier  = Modifier.height(5.dp))
                    }
                    items(appViewModel.accounts){
                        if(it.title.lowercase().contains(inp.lowercase()) || it.email.lowercase().contains(inp.lowercase())){
                            var moreEnabled = rememberSaveable { mutableStateOf(false) }
                            var updateEnabled = rememberSaveable { mutableStateOf(false) }
                            Entry(data = it, { id ->
                                if (id == curr.value) curr.value = -1L
                                else curr.value = id
                            }, curr.value, { moreEnabled.value = true }) {
                                VerticalDrop(
                                    expanded = moreEnabled.value,
                                    onDismiss = { moreEnabled.value = false },
                                    onDelete = { appViewModel.deleteAccounts(context, it.id) },
                                    onUpdate = {updateEnabled.value = true})
                            }
                            if(updateEnabled.value)
                                Dialog(onDismissRequest = { updateEnabled.value = false }) {
                                    ReUpdateAcc(appViewModel,it) { updateEnabled.value = false }
                                }

                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    items(appViewModel.cards){
                        if(it.title.lowercase().contains(inp.lowercase())){
                            var moreEnabled = rememberSaveable{ mutableStateOf(false) }
                            var updateEnabled = rememberSaveable { mutableStateOf(false) }
                            CardEntry(data = it,{id ->
                                if(id+100000 == curr.value) curr.value = -1L
                                else curr.value = id+100000
                            },curr.value , {moreEnabled.value  = true}){
                                VerticalDrop(
                                    expanded = moreEnabled.value,
                                    onDismiss = { moreEnabled.value = false },
                                    onDelete = { appViewModel.deleteCards(context,it.id) },
                                    onUpdate = { updateEnabled.value = true })
                            }
                            if(updateEnabled.value)
                                Dialog(onDismissRequest = { updateEnabled.value = false }) {
                                    ReUpdateCard(appViewModel,it) { updateEnabled.value = false }
                                }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    if(appViewModel.accounts.size == 0 && appViewModel.cards.size == 0)
                        item {
                            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
                                Spacer(Modifier.height(200.dp))
                                Image(painterResource(id = R.drawable.add),null , modifier = Modifier.size(100.dp))
                                Spacer(Modifier.height(10.dp))
                                Text("Add Something First",color = clrScheme.onPrimary)
                            }
                        }

                }


            }

        }
    }
    AnimatedVisibility(visible = isDiag ) {
        SaveAcc (appViewModel,{isDiag = false} ,
            onAccAdd = {
                 if(data.title.isBlank()){
                     Toast.makeText(context ,"Empty Fields" , Toast.LENGTH_SHORT).show()
                 }else {
                     appViewModel.addAccount(context)
                     isDiag = false
                 }
        },
            onCardAdd = {
                if(dataCard.title.isBlank() || dataCard.cardNumber.isBlank() || dataCard.cardNumber.length < 16 ||
                    dataCard.validity.isBlank() || dataCard.validity.length < 4
                    ){
                     Toast.makeText(context ,"Invalid Entry(ies)" , Toast.LENGTH_SHORT).show()
                }else {
                    if(dataCard.cvv.isBlank()) appViewModel.updateInpCard(4,"***")
                    appViewModel.addCards(context)
                    isDiag = false
                }
            })
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SaveAcc(appViewModel: AppViewModel, onDismiss:()->Unit, onAccAdd:()->Unit, onCardAdd:()->Unit){
    val context = LocalContext.current
    val data = appViewModel._inputAccount.collectAsState().value
    val dataCard = appViewModel._inputCard.collectAsState().value
    var isAcc by remember {
        mutableStateOf(true)
    }
    Dialog(onDismissRequest = onDismiss , properties = DialogProperties(dismissOnClickOutside = true)) {
        Column{
            Row(modifier = Modifier) {
                Column(modifier = Modifier
                    .clip(RoundedCornerShape(29.dp, 29.dp, 0.dp, 0.dp))
                    .clickable { isAcc = true }
                    .background(
                        if (isAcc) clrScheme.onSecondary else clrScheme.onPrimary,
                        RoundedCornerShape(29.dp, 29.dp, 0.dp, 0.dp)
                    )
                    .padding(12.dp)
                    .weight(1f) ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text("ACCOUNT",fontWeight = FontWeight.Bold , color = if(isAcc)clrScheme.onPrimary else clrScheme.onBackground)
                }
                Spacer(Modifier.width(7.dp))
                Column(modifier = Modifier
                    .clip(RoundedCornerShape(29.dp, 29.dp, 0.dp, 0.dp))
                    .clickable { isAcc = false }
                    .background(
                        if (!isAcc) clrScheme.onSecondary else clrScheme.onPrimary,
                        RoundedCornerShape(29.dp, 29.dp, 0.dp, 0.dp)
                    )
                    .padding(12.dp)
                    .weight(1f) ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text("CARD",fontWeight = FontWeight.Bold , color = if(!isAcc)clrScheme.onPrimary else clrScheme.onBackground)
                }

            }
            if(isAcc)
                Column(modifier = Modifier
                    .background(clrScheme.onSecondary, RoundedCornerShape(0.dp, 0.dp, 29.dp, 29.dp))
                    .padding(18.dp)
                    , horizontalAlignment = Alignment.CenterHorizontally ){
                    FillText(data = data.title, "Title:") {
                        appViewModel.updateInp(1 , it)
                    }
                    Spacer(Modifier.height(15.dp))
                    FillText(data = data.userName, "User ID :") {
                        appViewModel.updateInp(2 , it)
                    }
                    Spacer(Modifier.height(15.dp))
                    FillText(data = data.email, "Email or Phone :") {
                        appViewModel.updateInp(3 ,it )
                    }
                    Spacer(Modifier.height(15.dp))
                    FillText(data = data.pass,  "Password :") {
                        appViewModel.updateInp(4 , it)
                    }
                    Spacer(Modifier.height(25.dp))
                    Button(onClick = onAccAdd , modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                listOf(clrScheme.onPrimary, clrScheme.onPrimary)
                            ), RoundedCornerShape(40.dp)
                        )
                        .height(45.dp)
                        .width(220.dp)
                        ,
                        colors =  ButtonDefaults.buttonColors( Color.Transparent ),
                    ){
                        Text("ADD ACCOUNT" , color = clrScheme.onSecondary)

                    }
                }
            else
                Column(modifier = Modifier
                    .background(clrScheme.onSecondary, RoundedCornerShape(0.dp, 0.dp, 29.dp, 29.dp))
                    .padding(18.dp)
                    , horizontalAlignment = Alignment.CenterHorizontally ){
                    FillText(data = dataCard.title, "Title:") {
                        appViewModel.updateInpCard(1 , it)
                    }
                    Spacer(Modifier.height(15.dp))
                    FillText(data = dataCard.cardNumber, "Card Number:",KeyboardOptions(keyboardType = KeyboardType.Number , imeAction = ImeAction.Next)) {
                        appViewModel.updateInpCard(2 , it)
                    }
                    Spacer(Modifier.height(15.dp))
                    FillText(data = dataCard.validity, "Validity:",KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)) {
                        appViewModel.updateInpCard(3 ,it )
                    }
                    Spacer(Modifier.height(15.dp))
                    FillText(data = dataCard.cvv,  "CVV:",KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)) {
                        appViewModel.updateInpCard(4 , it)
                    }
                    Spacer(Modifier.height(25.dp))
                    Button(onClick = onCardAdd , modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                listOf(clrScheme.onPrimary, clrScheme.onPrimary)
                            ), RoundedCornerShape(30.dp)
                        )
                        .height(45.dp)
                        .width(220.dp)
                        ,
                        colors =  ButtonDefaults.buttonColors( Color.Transparent ),
                    ){
                        Text("ADD CARD" , color = clrScheme.onSecondary)

                    }
                }
        }



    }
}


@Composable
fun VerticalDrop(expanded:Boolean,onDismiss: () -> Unit,onDelete:()->Unit,onUpdate:()->Unit ){
    MaterialTheme(
        shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(20.dp)) ,
    ) {
        DropdownMenu(expanded = expanded, onDismissRequest = onDismiss ,
            modifier = Modifier
                .background(clrScheme.onBackground),
            properties = PopupProperties(dismissOnClickOutside = true) ,
        ){
            DropdownMenuItem(text = {
                Row(verticalAlignment = Alignment.CenterVertically){
                    Image(painterResource(R.drawable.loop) , null, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(9.dp))
                    Text(text = "UPDATE",color = clrScheme.onPrimary , fontWeight = FontWeight.Bold)
                }
            }, onClick = onUpdate)
            DropdownMenuItem(text = {
                Row(verticalAlignment = Alignment.CenterVertically){
                    Image(painterResource(R.drawable.delete) , null  , modifier = Modifier.size(27.dp))
                    Spacer(Modifier.width(9.dp))
                    Text(text = "DELETE",color = clrScheme.onPrimary , fontWeight = FontWeight.Bold)
                }

            }, onClick = onDelete)

        }
    }


}
