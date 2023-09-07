package com.cyrax.passwordmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cyrax.passwordmanager.Components.All
import com.cyrax.passwordmanager.Components.AppViewModel
import com.cyrax.passwordmanager.Components.Lock
import com.cyrax.passwordmanager.Components.SetLock
import com.cyrax.passwordmanager.ui.theme.PasswordManagerTheme
import com.cyrax.passwordmanager.ui.theme.clrScheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PasswordManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val appViewModel: AppViewModel = viewModel()
                    val context = LocalContext.current
                    LaunchedEffect(Unit){
                        appViewModel.getActualPin(context)
                    }
                    if(!appViewModel.wait.collectAsState().value)
                    AnimatedContent(targetState = appViewModel.PIN.collectAsState().value == if(appViewModel.actualPIN?.pin == null)"" else appViewModel.actualPIN!!.pin,
                        label = "",   transitionSpec = {
                        fadeIn() + slideInVertically(animationSpec = tween(400),
                            initialOffsetY = { fullHeight -> fullHeight }) with
                                fadeOut(animationSpec = tween(200))

                    }) {
                        if(!it) Lock(appViewModel)
                        else Drawer(appViewModel)
                    }

                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun Drawer(appViewModel: AppViewModel){
    val context = LocalContext.current
    var isSetPin by remember{ mutableStateOf(false) }
    var drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            Column(horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier
                .fillMaxWidth(.80f)
                .fillMaxHeight()
                .background(clrScheme.onSecondary, RoundedCornerShape(0.dp, 13.dp, 13.dp, 0.dp))){
                Spacer(Modifier.height(20.dp))
                Row( modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        isSetPin = true
                        scope.launch(Dispatchers.IO) {
                            drawerState.close()
                        }
                    },verticalAlignment = Alignment.CenterVertically ,){
                    Spacer(Modifier.width(35.dp))
                    Image(painterResource(R.drawable.security) , null, modifier = Modifier.size(40.dp))
                    Spacer(Modifier.width(25.dp))
                    Column{
                        Text(text = "Set PIN" , color = clrScheme.onPrimary , fontSize = 20.sp , fontWeight = FontWeight.Black)
                        Text(text = "Add an extra layer of protection ;>" , color = clrScheme.onPrimary , fontSize = 10.sp )
                    }

                }

            }
        }

    ) {
        LaunchedEffect(Unit){
            appViewModel.getCards(context)
            appViewModel.getAccounts(context)
        }
        AnimatedVisibility(visible = !isSetPin, enter = fadeIn() , exit = fadeOut() ) {
                All(appViewModel = appViewModel) {
                    scope.launch(Dispatchers.IO){
                        drawerState.open()
                    }
                }
        }
        AnimatedVisibility(visible = isSetPin, enter = fadeIn() , exit = fadeOut() ) {
            SetLock(appViewModel) { isSetPin = false }
        }



    }

}

