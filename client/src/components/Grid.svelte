<script>
    import GridSquare from "./GridSquare.svelte";

    export let connection = null;
    export let icon = "";
</script>

<div class="grid">
    {#each Array(9) as _, index (index)}
        <GridSquare index={index} connection={connection} icon={icon}/>
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
        margin: 2.5em;
    }
</style>