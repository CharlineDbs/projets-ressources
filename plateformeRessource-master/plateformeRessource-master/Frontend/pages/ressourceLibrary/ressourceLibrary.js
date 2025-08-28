<script>
  const buttons = document.querySelectorAll(".filters button");
  const cards = document.querySelectorAll(".resource-card");

  buttons.forEach(btn => {
    btn.addEventListener("click", () => {
      // Active button style
      buttons.forEach(b => b.classList.remove("active"));
      btn.classList.add("active");

      const categoryFilter = document.querySelector(".filters .active[data-filter]").getAttribute("data-filter");
      const relationFilter = btn.getAttribute("data-filter");
      const documentFilter = btn.getAttribute("data-filter");

      cards.forEach(card => {
        const category = card.getAttribute("data-category");
        const relation = card.getAttribute("data-relation");
        const documentType = card.getAttribute("data-document");

        const showCategory = categoryFilter === "all" || category === categoryFilter;
        const showRelation = relationFilter === "all" || relation === relationFilter;
        const showDocument = documentFilter === "all" || documentType === documentFilter;

        if (showCategory && showRelation && showDocument) {
          card.style.display = "block";
        } else {
          card.style.display = "none";
        }
      });
    });
  });
</script>
