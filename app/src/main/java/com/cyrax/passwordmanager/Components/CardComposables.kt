package com.cyrax.passwordmanager.Components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyrax.passwordmanager.R
import com.cyrax.passwordmanager.db.Accounts
import com.cyrax.passwordmanager.db.Cards
import com.cyrax.passwordmanager.ui.theme.clrScheme

@Composable
fun CardEntry(data: Cards, onClick:(Long)->Unit, currId:Long, onMoreVert:()->Unit,DDMenu:@Composable()(()->Unit)){

    Column(modifier = Modifier
        .shadow(3.dp , RoundedCornerShape(20.dp))
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
                Text(text = data.title , fontWeight = FontWeight.Bold , color = clrScheme.onPrimary)
                Spacer(modifier = Modifier.height(3.dp))
                Text(text = "Last Updated: ${tsToDate(data.lastUpdated)}",fontWeight = FontWeight.Light , color = Color.Gray, fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopEnd)){
                IconButton(onClick = onMoreVert) {
                    Image( Icons.Rounded.MoreVert, contentDescription = null,
                        Modifier.size(30.dp), colorFilter = ColorFilter.tint(
                            clrScheme.background))
                }
                DDMenu()
            }


        }
        AnimatedVisibility(visible = data.id == (currId-100000) ){
            Spacer(Modifier.height(3.dp))
            Column(modifier = Modifier.padding(55.dp,10.dp), horizontalAlignment = Alignment.CenterHorizontally){
                DispCardText(data = data.cardNumber,"Card Number :")
                Spacer(Modifier.height(8.dp))
                Row {
                    DispCardText(data = "${data.validity.substring(0,2)} ${data.validity.substring(2)}","Validity :")
                    Spacer(Modifier.weight(1f))
                    DispCardText(data = data.cvv, "CVV :")
                }

                Spacer(Modifier.height(20.dp))
            }
        }


    }

}



@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DispCardText(data:String,itIs:String){
    val context  = LocalContext.current
    var notShow by remember{ mutableStateOf(true) }
    Column(
    ){
        Row{
            Spacer(Modifier.width(4.dp))
            Text(text = itIs , fontSize = 10.sp , color = clrScheme.onPrimary)
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
                AnimatedContent(targetState = notShow, label = "") {
                    when(itIs){
                        "Card Number :" -> {
                            if(it) Text(text = "${data.substring(0,4)}  ****  ****  ****", modifier = Modifier , color = clrScheme.onPrimary )
                            else Text(text =  "${data.substring(0,4)} ${data.substring(4,8)} ${data.substring(8,12)} ${data.substring(12)}", modifier = Modifier , color = clrScheme.onPrimary )
                        }
                        "Validity :" ->{
                            if(it) Text(text = "**  **", modifier = Modifier , color = clrScheme.onPrimary )
                            else Text(text =  "${data.substring(0,2)} ${data.substring(2)}", modifier = Modifier , color = clrScheme.onPrimary )
                        }
                        else -> {
                            if(it) Text(text = " *** ", modifier = Modifier , color = clrScheme.onPrimary )
                            else Text(text =  data, modifier = Modifier , color = clrScheme.onPrimary )
                        }
                    }

                }
                if(itIs == "Card Number :")Spacer(modifier = Modifier.weight(1f))

            }
        }
    }


}


@Composable
fun ReUpdateAcc(appViewModel: AppViewModel, dataa:Accounts, closeDiag:()->Unit){
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit){
        appViewModel.updateAccStore(100,"",dataa)
    }
    var data  = appViewModel.reFillAccount.collectAsState().value
    Column(modifier = Modifier
        .background(clrScheme.onSecondary, RoundedCornerShape(30.dp, 30.dp, 29.dp, 29.dp))
        .padding(18.dp)
        , horizontalAlignment = Alignment.CenterHorizontally ){
        FillText(data = data.title, "Title:") {
            appViewModel.updateAccStore(1,it,dataa)
        }
        Spacer(Modifier.height(15.dp))
        FillText(data = data.userName, "User ID :") {
            appViewModel.updateAccStore(2,it,dataa)
        }
        Spacer(Modifier.height(15.dp))
        FillText(data = data.email, "Email or Phone :") {
            appViewModel.updateAccStore(3,it,dataa)
        }
        Spacer(Modifier.height(15.dp))
        FillText(data = data.pass,  "Password :") {
            appViewModel.updateAccStore(4,it,dataa)
        }
        Spacer(Modifier.height(25.dp))
        Button(onClick = {
                         appViewModel.updateAccount(context , data)
                         closeDiag()
        } , modifier = Modifier
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
            Text("UPDATE ACCOUNT" , color = clrScheme.onSecondary)

        }
    }
}




@Composable
fun ReUpdateCard(appViewModel: AppViewModel, data :Cards, closeDiag:()->Unit){
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit){
        appViewModel.updateCardStore(100,"",data)
    }
    var dataCard = appViewModel.reFillCard.collectAsState().value
    Column(modifier = Modifier
        .background(clrScheme.onSecondary, RoundedCornerShape(30.dp, 30.dp, 29.dp, 29.dp))
        .padding(18.dp)
        , horizontalAlignment = Alignment.CenterHorizontally ){
        FillText(data = dataCard.title, "Title:") {
            appViewModel.updateCardStore(1,it,data)
        }
        Spacer(Modifier.height(15.dp))
        FillText(data = dataCard.cardNumber, "Card Number:",KeyboardOptions(keyboardType = KeyboardType.Number , imeAction = ImeAction.Next)) {
            appViewModel.updateCardStore(2,it,data)
        }
        Spacer(Modifier.height(15.dp))
        FillText(data = dataCard.validity, "Validity:",KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)) {
            appViewModel.updateCardStore(3,it,data)
        }
        Spacer(Modifier.height(15.dp))
        FillText(data = dataCard.cvv,  "CVV:",KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)) {
            appViewModel.updateCardStore(4,it,data)
        }
        Spacer(Modifier.height(25.dp))
        Button(onClick = {
            appViewModel.updateCards(context, dataCard)
            closeDiag()
        }, modifier = Modifier
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
            Text("UPDATE CARD" , color = clrScheme.onSecondary)

        }
    }
}