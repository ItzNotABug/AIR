package com.lazygeniouz.air.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazygeniouz.air.R
import com.lazygeniouz.air.data.viewmodel.AdIdViewModel
import com.lazygeniouz.air.utils.misc.isGmsInstalled
import com.lazygeniouz.air.utils.misc.toast

/**
 * A [Column] widget with centered alignments.
 */
@Composable
fun CenteredColumn(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier,
        content = content,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
}

/**
 * A [Button] widget that simply resets / deletes the file containing the Advertising Identifier.
 */
@Composable
fun ResetAdIdButton(viewModel: AdIdViewModel, onResult: (Boolean) -> Unit) {
    val context = LocalContext.current
    val adIdResetPrefixText = stringResource(R.string.adid_reset_prefix)

    Button(modifier = Modifier.padding(12.dp), onClick = {
        val isSuccess = viewModel.deleteAdIdFile()
        val messageStatus = if (isSuccess) "Successfully" else "Failed"
        context.toast(String.format(adIdResetPrefixText, messageStatus))
        onResult(isSuccess)
    }) {
        Text(
            modifier = Modifier.padding(4.dp),
            text = stringResource(R.string.reset_ad_button_text),
        )
    }
}

/**
 * A [Button] widget that deletes the stored path of the file that contains the Advertising Identifier &
 * allows the user to start over on Non GMS Devices.
 */
@Composable
fun ResetAdIdFilePath(viewModel: AdIdViewModel, onCompletion: () -> Unit) {
    Button(modifier = Modifier.padding(12.dp), onClick = {
        viewModel.deleteAdIdFilePath()
        onCompletion()
    }) {
        Text(
            modifier = Modifier.padding(4.dp),
            text = stringResource(R.string.paste_adid_button_text),
        )
    }
}

/**
 * A [Text] widget that simply informs that the Advertising Identifier is not available right now.
 */
@Composable
fun AdIdentifierDoesNotExistsText() {
    Text(
        fontWeight = FontWeight.W500,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(12.dp),
        text = stringResource(R.string.adid_does_not_exist),
    )
}

/**
 * A [Text] widget that simply displays the Advertising Identifier.
 */
@Composable
fun AdIdentifierText(adId: String) {
    SelectionContainer {
        Text(
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(12.dp),
            text = stringResource(R.string.adid, adId),
        )
    }
}

/**
 * A [Text] widget that displays whether the device has GMS installed or not.
 */
@Composable
fun GmsInstallationText() {
    val context = LocalContext.current
    val isGmsInstalled = remember { context.isGmsInstalled() }
    val displayTextResource = remember {
        if (isGmsInstalled) R.string.gms_installed else R.string.gms_not_installed
    }

    Text(modifier = Modifier.padding(12.dp), text = stringResource(displayTextResource))
}

/**
 * A Form based widget that takes the Advertising Identifier as an input to traverse the system
 * directories to find the file containing the identifier.
 */
@Composable
fun PasteAdIdentifierForm(viewModel: AdIdViewModel, onResult: (String) -> Unit) {
    val context = LocalContext.current
    var inputAdId by remember { mutableStateOf("38400000-8cf0-11bd-b23e-10b96e40000d") }
    val inputValid by remember { derivedStateOf { inputAdId.isNotEmpty() && inputAdId.length == 36 } }

    val adIdFileFoundText = stringResource(R.string.adid_file_found)
    val adIdFileNotFoundText = stringResource(R.string.adid_file_not_found)

    CenteredColumn {
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(18.dp),
            text = stringResource(R.string.paste_adid),
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 24.dp),
            value = inputAdId,
            placeholder = {
                Text(
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.LightGray.copy(alpha = 0.5f),
                    text = stringResource(R.string.placeholder_adid),
                )
            },
            onValueChange = { newValue -> inputAdId = newValue },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 14.sp
            ),
        )

        Button(
            modifier = Modifier.padding(12.dp), onClick = {
                viewModel.searchForAdIdFile(inputAdId) { filePath ->
                    if (filePath.isNotEmpty()) {
                        onResult(filePath)
                        context.toast(adIdFileFoundText)
                    } else context.toast(adIdFileNotFoundText)
                }
            }, enabled = inputValid
        ) {
            Text(text = stringResource(R.string.check), modifier = Modifier.padding(4.dp))
        }
    }
}

/**
 * [Text] widgets that are displayed when the device does not have Root Access or
 * is not Granted permission from the Root Manager Ap.
 */
@Composable
fun RootNotAvailable() {
    CenteredColumn {
        Text(
            color = Color.Red,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(12.dp),
            text = stringResource(R.string.root_access_required),
        )

        Text(
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(12.dp),
            style = TextStyle(textAlign = TextAlign.Center),
            text = stringResource(R.string.root_access_required_desc),
        )
    }
}