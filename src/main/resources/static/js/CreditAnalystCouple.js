function fillFormFields(button) {
    // Obtener la fila correspondiente
    var row = button.closest("tr");

    // Obtener los datos de la fila
    var taskId = row.querySelector("td:nth-child(1)").textContent;
    var coupleName = row.querySelector("td:nth-child(2)").textContent;
    var requestDate = row.querySelector("td:nth-child(3)").textContent;
    var state = row.querySelector("td:nth-child(4)").textContent;
    var countReviewsCS = row.querySelector("td:nth-child(5)").textContent;
    var coupleEmail1 = row.querySelector("td:nth-child(6)").textContent;
    var coupleEmail2 = row.querySelector("td:nth-child(7)").textContent;
    var marriageYears = row.querySelector("td:nth-child(8)").textContent;
    var bothEmployees = row.querySelector("td:nth-child(9)").textContent;

    // Llenar los campos del formulario
    document.getElementById("taskId").value = taskId;
    document.getElementById("CoupleName").value = coupleName;
    document.getElementById("requestDate").value = requestDate;
    document.getElementById("state").value = state;
    document.getElementById("countReviewsCS").value = countReviewsCS;
    document.getElementById("coupleEmail1").value = coupleEmail1;
    document.getElementById("coupleEmail2").value = coupleEmail2;
    document.getElementById("marriageYears").value = marriageYears;
    document.getElementById("bothEmployees").value = bothEmployees;
    document.getElementById("bothEmployees").setAttribute("readonly", "readonly");

}


    document.addEventListener("DOMContentLoaded", function () {
    // Obtener el formulario y el botón de aprobación
        var form = document.querySelector("form");
        var approveButton = document.getElementById("approveButton");
        var rejectedButton = document.getElementById("rejectedButton");

        // Agregar un controlador de clic al botón de aprobación
        approveButton.addEventListener("click", function (event) {
            event.preventDefault(); // Evitar que el formulario se envíe automáticamente
            form.action="/approve-credit-analyst-couple"
            // Mostrar la alerta con un indicador de carga al inicio del envío del formulario
            Swal.fire({
                position: 'center',
                icon: 'info',
                title: 'sending response',
                timerProgressBar: true,
                showConfirmButton: false,
                timer: 2500
            }).then((result) => {
                /* Read more about handling dismissals below */
                if (result.dismiss === Swal.DismissReason.timer) {
                    Swal.fire({
                        position: 'center',
                        icon: 'success',
                        title: 'successful sending!',
                        showConfirmButton: false,
                    })
                }
            })
            // Enviar el formulario
            form.submit();
        });


        // Agregar un controlador de clic al botón de aprobación
        rejectedButton.addEventListener("click", function (event) {
            event.preventDefault(); // Evitar que el formulario se envíe automáticamente
            form.action="/rejected-credit-analyst-couple"

            // Mostrar la alerta con un indicador de carga al inicio del envío del formulario
            Swal.fire({
                position: 'center',
                icon: 'info',
                title: 'sending response',
                timerProgressBar: true,
                showConfirmButton: false,
                timer: 2500
            }).then((result) => {
                /* Read more about handling dismissals below */
                if (result.dismiss === Swal.DismissReason.timer) {
                    Swal.fire({
                        position: 'center',
                        icon: 'success',
                        title: 'successful sending!',
                        showConfirmButton: false,
                    })
                }
            })
            // Enviar el formulario
            form.submit();
        });
    });
