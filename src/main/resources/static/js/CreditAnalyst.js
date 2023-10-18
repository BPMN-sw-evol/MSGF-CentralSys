function fillFormFields(button) {
    // Obtener la fila correspondiente
    var row = button.closest("tr");

    // Obtener los datos de la fila
    var taskId = row.querySelector("td:nth-child(1)").textContent;
    var coupleName = row.querySelector("td:nth-child(2)").textContent;
    var requestDate = row.querySelector("td:nth-child(3)").textContent;
    var marriageYears = row.querySelector("td:nth-child(4)").textContent;
    var bothEmployees = row.querySelector("td:nth-child(5)").textContent;
    var housePrices = row.querySelector("td:nth-child(6)").textContent;
    var quotaValue = row.querySelector("td:nth-child(7)").textContent;
    var coupleSavings = row.querySelector("td:nth-child(8)").textContent;




    // Llenar los campos del formulario
    document.getElementById("taskId").value = taskId;
    document.getElementById("CoupleName").value = coupleName;
    document.getElementById("requestDate").value = requestDate;
    document.getElementById("marriageYears").value = marriageYears;
    document.getElementById("bothEmployees").value = bothEmployees;

    // Obtener el contenedor del campo
    var housePricesContainer = document.getElementById("housePricesContainer");
    var quotaValueContainer = document.getElementById("quotaValueContainer");
    var coupleSavingsContainer = document.getElementById("coupleSavingsContainer");


    // Mostrar u ocultar el contenedor según el valor
    if (housePrices === "0") {
        housePricesContainer.style.display = "none";
    } else {
        housePricesContainer.style.display = "block";
        document.getElementById("housePrices").value = housePrices;
    }

    if (housePrices === "0") {
        quotaValueContainer.style.display = "none";
    } else {
        quotaValueContainer.style.display = "block";
        document.getElementById("quotaValue").value = quotaValue;
    }

    if (housePrices === "0") {
        coupleSavingsContainer.style.display = "none";
    } else {
        coupleSavingsContainer.style.display = "block";
        document.getElementById("coupleSavings").value = coupleSavings;
    }

    // Establecer el valor de assignee
    var assignee = "CreditAnalyst";
    if (housePrices !== "0" && quotaValue !== "0" && coupleSavings !== "0") {
        assignee = "CreditCommitte";
    }
    document.getElementById("assignee").value = assignee;

}


    document.addEventListener("DOMContentLoaded", function () {
    // Obtener el formulario y el botón de aprobación
        var form = document.querySelector("form");
        var approveButton = document.getElementById("approveButton");


        document.getElementById("isValid").value = "true";

    // Agregar un controlador de clic al botón de aprobación
        approveButton.addEventListener("click", function (event) {
            event.preventDefault(); // Evitar que el formulario se envíe automáticamente

    // Enviar el formulario
            form.submit();
        });
    });
