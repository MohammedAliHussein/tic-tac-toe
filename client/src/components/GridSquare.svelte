<script>
    export let index = null;
    let borders = "";

    function handleSquareChoice() {}

    function determineBorders(index) {
        if (index == 0) borders = "top-left";
        if (index == 1 || index == 2) borders = "top";
        if (index % 3 == 0 && index <= 6 && index !== 0) borders = "left";
    }

    determineBorders(index);
</script>

<div class={`grid-square ${borders}`} on:click={handleSquareChoice}></div>

<style>
    .grid-square {
        cursor: pointer;
        transition: 0.2s cubic-bezier(0, 0.55, 0.45, 1);
        width: 170px;
        height: 170px;
        border-right: 1px solid rgba(255, 255, 255, 0.053);
        border-bottom: 1px solid rgba(255, 255, 255, 0.053);
    }

    .grid-square:hover {
        background-color: rgba(255, 255, 255, 0.01);
    }

    .left {
        border-left: 1px solid rgba(255, 255, 255, 0.053);
    }

    .top {
        border-top: 1px solid rgba(255, 255, 255, 0.053);
    }

    .top-left {
        border-top: 1px solid rgba(255, 255, 255, 0.053);
        border-left: 1px solid rgba(255, 255, 255, 0.053);
    }
</style>