<script>
    import { onMount } from "svelte";

    import Modals from "./Modals.svelte";
    import GridSquare from "./GridSquare.svelte";

    export let connection = null;
    export let icon = "";
    export let displayName = "";

    let tie = false;
    let win = false;
    let disconnect = false;
    let showingModal = false;

    let winner = "";

    function handleMessage(event) {
        const message = JSON.parse(event.data);

        setTimeout(() => {
            switch(message.type) {
                case "tie":
                    tie = true;
                    showingModal = true;
                    break;
                case "win":
                    showingModal = true;
                    winner = message.player;
                    win = true;
                    break;
                case "disconnect":
                    showingModal = true;
                    disconnect = true;
                    break;
            }
        }, 800) //wait for winning moves change
    }
    
    onMount(() => {
        connection.addEventListener("message", handleMessage);
    });
</script>

<div class="grid-container">
    {#if showingModal}
        <div class="modals">
            <Modals win={win} tie={tie} winner={winner} disconnect={disconnect}/>
        </div>
    {/if}
    <div class="grid">
        {#each Array(9) as _, index (index)}
            <GridSquare index={index} connection={connection} icon={icon} displayName={displayName}/>
        {/each}
    </div>
</div>

<style>
    .grid-container {
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .grid {
        display: flex;
        justify-content: center;
        align-items: center;
        flex-wrap: wrap;
        width: 510px;
        height: 510px;
        margin: 2.5em;
    }

    .modals {
        position: absolute;
        width: 100%;
        height: 100vh;
        background: none;
        top: 0;
        z-index: 10;
    }
</style>