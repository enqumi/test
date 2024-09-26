package com.example.shoppinglisttwooseven

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun MainScreen() {
    var index by remember { mutableIntStateOf(1) }
    var listOfShoppingItems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var isAdding by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<ShoppingItem?>(null) }

    if (isAdding) {
        AddDialog(
            index = index,
            onDismissRequest = { isAdding = false },
            onAddClicked = {
                listOfShoppingItems += it
                index++
                isAdding = false
            }
        )
    }

    itemToEdit?.let { shoppingItem ->
        EditDialog(
            shoppingItem = shoppingItem,
            onDismissRequest = { itemToEdit = null },
            onEditCompleted = { name, description, quantity ->
                listOfShoppingItems = listOfShoppingItems.map {
                    if (it.id == shoppingItem.id) it.copy(title = name, description = description, quantity = quantity)
                    else it
                }
                itemToEdit = null
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp)
            .background(Color(0xFFA5D6A7)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier.padding(vertical = 8.dp),
            onClick = { isAdding = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text("Добавить", color = Color.Black)
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(listOfShoppingItems) { element ->
                ShoppingItemCard(
                    shoppingItem = element,
                    onEditClicked = { itemToEdit = element },
                    onDeleteClicked = { listOfShoppingItems -= it }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDialog(index: Int, onDismissRequest: () -> Unit, onAddClicked: (ShoppingItem) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(1) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(onClick = {
                onAddClicked(ShoppingItem(id = index, title = title, description = description, quantity = quantity))
                onDismissRequest()

            }) { Text("Add") }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) { Text("Cancel")

            }
        },
        title = { Text("Add Shopping Item") },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Title") }
                )
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Description") }
                )
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = quantity.toString(),
                    onValueChange = { quantity = it.toIntOrNull() ?: 1 },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text("Quantity") }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDialog(shoppingItem: ShoppingItem, onDismissRequest: () -> Unit, onEditCompleted: (String, String, Int) -> Unit) {
    var editedName by remember { mutableStateOf(shoppingItem.title) }
    var editedDescription by remember { mutableStateOf(shoppingItem.description) }
    var editedQuantity by remember { mutableIntStateOf(shoppingItem.quantity) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(onClick = {
                onEditCompleted(editedName, editedDescription, editedQuantity)
                onDismissRequest()
            }) { Text("Save") }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) { Text("Cancel") }
        },
        title = { Text("Edit Shopping Item") },
        text = {
            Column {
                TextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    placeholder = { Text("Title") }
                )
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = editedDescription,
                    onValueChange = { editedDescription = it },
                    placeholder = { Text("Description") }
                )
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = editedQuantity.toString(),
                    onValueChange = { editedQuantity = it.toIntOrNull() ?: 1 },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text("Quantity") }
                )
            }
        }
    )
}

@Composable
fun ShoppingItemCard(shoppingItem: ShoppingItem, onEditClicked: () -> Unit, onDeleteClicked: (ShoppingItem) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC8E6C9))
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f).padding(8.dp)) {
                Text(text = shoppingItem.title, fontWeight = FontWeight.Bold)
                Text(text = shoppingItem.description)
                Text(text = "Quantity: ${shoppingItem.quantity}")
            }
            IconButton(onClick = onEditClicked) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = { onDeleteClicked(shoppingItem) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    ShoppingItemCard(ShoppingItem(1, "Test Item", "This is a test", 2), onEditClicked = {}) {}
}
