(() => {
    const table = document.getElementById("invoice-lines");
    const addButton = document.getElementById("add-line");
    const template = document.getElementById("line-template");

    if (!table || !addButton || !template) {
        return;
    }

    addButton.addEventListener("click", () => {
        const newRow = template.content.cloneNode(true);
        table.appendChild(newRow);
    });

    table.addEventListener("click", (event) => {
        if (!event.target.matches(".remove-line")) {
            return;
        }

        const rows = table.querySelectorAll("tr");

        if (rows.length === 1) {
            alert("An invoice requires at least one line item.");
            return;
        }

        event.target.closest("tr").remove();
    });
})();