function fillFormFields(button) {
    // Obtener la fila correspondiente
    var row = button.closest("tr");

    // Obtener los datos de la fila
    var taskId = row.querySelector("td:nth-child(1)").textContent;
    var coupleName = row.querySelector("td:nth-child(2)").textContent;
    var startDate = row.querySelector("td:nth-child(3)").textContent;

    // Llenar los campos del formulario
    document.getElementById("taskId").value = taskId;
    document.getElementById("CoupleName").value = coupleName;
    document.getElementById("StartDate").value = startDate;
}

document.addEventListener("DOMContentLoaded", function () {
// Obtener el formulario y el botón de aprobación
    var form = document.querySelector("form");
    var approveButton = document.getElementById("approveButton");


    document.getElementById("assignee").value = "CreditComitte";
    document.getElementById("isValid").value = "true";

// Agregar un controlador de clic al botón de aprobación
    approveButton.addEventListener("click", function (event) {
        event.preventDefault(); // Evitar que el formulario se envíe automáticamente



// Enviar el formulario
        form.submit();
    });
});
