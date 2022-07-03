<script>
    import GridSquare from "./GridSquare.svelte";
</script>

<div class="grid">
    {#each Array(9) as _, index (index)}
        <GridSquare index={index}/>
    {/each}
</div>

<style>
    .grid {
        display: flex;
        justify-content: center;
        align-items: center;
        flex-wrap: wrap;
        width: 510px;
        height: 510px;
    }
</style>