package com.oguz.cryptoapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Surface
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.oguz.cryptoapp.model.CryptoListItem
import com.oguz.cryptoapp.viewmodel.CryptoListViewModel


@Composable
fun CryptoListScreen(
    navController: NavController,
    viewModel: CryptoListViewModel = hiltViewModel()
) {
    Surface(
        color = MaterialTheme.colors.secondary,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Text("Crypto Crazy",modifier = Modifier
                .fillMaxWidth() //Modifier
                .padding(top = 20.dp),//Modifier
                textAlign = TextAlign.Center,
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold, //KAlın
                color = MaterialTheme.colors.primary //
            )
            Spacer(modifier = Modifier.height(10.dp))
            SearchBar(
                hint = "Search...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) { //TODO: it ->  onSearch: (String) -> Unit = {} bunu yazdımız ıcın verılıyor. Bana kod blogu versin diye koyduk.
                viewModel.searchCryptoList(it)
            }
            Spacer(modifier = Modifier.height(16.dp))
            //List
            CryptoList(navController = navController)
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {} //TODO :String alan bize unit veren boş bir fun
    //TODO:Search yaptıgımda bana bir kod blogu acacak ve yapılan aramayı string olarak bana verecek.
) {
    var text by remember {
        mutableStateOf("")
    }

    var isHintDisplayed by remember {
        mutableStateOf(hint != "") //TODO: Boş değilse true olacak true olarak baslayacak
    }

    Box(modifier = modifier) {
        BasicTextField(
            value = text, //TODO:Bu text i MutableStateof ile yapacagız ki kullanıcı yazarken degiskenimiz degisebilsin.
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {//TODO:Kullanıcı buraya tıklamayı bırakınca ne olsun hint diye bişey koyduk en başa onu kullanıcı buraya ne yazacıgını bılsın dıye o gosterılsın dıye bunu koyuyrouz.
                    isHintDisplayed = it.isFocused != true && text.isEmpty()
                    //TODO:Kullanıcı artık tıklamıyorsa ve searchbar boşsa hint gösterilecek
                }
        )
        if(isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}
//TODO: Listemin kendisi viewmodel ı burada kullanacagım.
@Composable
fun CryptoList(
    navController: NavController,
    viewModel: CryptoListViewModel = hiltViewModel()
) {
    val cryptoList by remember { viewModel.cryptoList }
    val errorMessage by remember { viewModel.errorMessage }
    val isLoading by remember { viewModel.isLoading }

    CryptoListView(cryptos = cryptoList,navController = navController)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if(isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if(errorMessage.isNotEmpty()) {
            RetryView(error = errorMessage) { //TODO: Tekrar deneyebilsin
                viewModel.downloadCryptos()
            }
        }
    }

}

@Composable
fun CryptoListView(cryptos: List<CryptoListItem>, navController: NavController) {
    //TODO:LazyColumn-> Recyclerview gibi verileri scroll ettikçe gösteren bir görünüm
    LazyColumn(contentPadding = PaddingValues(5.dp)) {
        items(cryptos) { crypto ->
            CryptoRow(navController = navController,crypto = crypto)
        }
    }
}

@Composable
fun CryptoRow(navController: NavController,crypto: CryptoListItem) {
    Box() {}
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colors.secondary)
        .clickable {
            navController.navigate(
                "crypto_detail_screen/${crypto.currency}/${crypto.price}"
            )
        }
    ) {
        Text(text = crypto.currency,
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(2.dp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary
        )
        Text(text = crypto.price,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(2.dp),
            color = MaterialTheme.colors.primaryVariant
        )
    }
}


/**Kullanıcı bir buton görecek Butona bastıgında onRetry diye bir fun çalıaşacak
 *
 */
@Composable
fun RetryView(
    error: String,
    onRetry: () -> Unit = {}
) {
    Column {
        Text(error, color = Color.Red, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}