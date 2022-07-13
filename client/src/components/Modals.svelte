<script>
    import { onMount } from "svelte";
    import { fade, fly } from "svelte/transition";
    import { circOut } from "svelte/easing";

    const DELAY = 700;

    export let winner = "";
    export let win = false;
    export let tie = false;
    export let disconnect = false;

    let counter = 5;

    function startCountdown() {
        let id = setInterval(() => {
            counter -= 5;
            if(counter == 0) clearInterval(id);
        }, 1000);
    }

    let ready = false;
    onMount(() => {
        ready = true;
        setTimeout(() => {
            startCountdown();
        }, DELAY);
    })
</script>

{#if ready}
    <div class="win-container" in:fade={{easing: circOut, duration: DELAY}}>
        {#if win}
            <div class="modal" in:fly={{delay: DELAY, y: 20, easing: circOut}}>
                <h2>{winner} wins</h2>
                <h4>Going back to home in {counter}</h4>
                <h5>[Replay a game feature coming...]</h5>
            </div>
        {:else if tie}
             <div class="modal" in:fly={{delay: DELAY, y: 20, easing: circOut}}>
                <h2>Game reached a tie</h2>
                <h4>Going back to home in {counter}</h4>
                <h5>[Replay a game feature coming...]</h5>
            </div>           
        {:else if disconnect}
            <div class="modal" in:fly={{delay: DELAY, y: 20, easing: circOut}}>
                <h2>Opponent has disconnected</h2>
                <h4>Going back to home in {counter}</h4>
                <h5>[Replay a game feature coming...]</h5>
            </div>
        {/if}
    </div>
{/if}

<style>
    .win-container {
        width: 100%;
        height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
        background: none;
        background-color: rgba(0, 0, 0, 0.699)    
    }

    .modal {
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
        background-color: rgb(7, 7, 7);
        padding: 15px 50px;
        border: 1px solid rgba(255, 255, 255, 0.064);
    }

    h2, h4, h5 {
        color: white;
        background: none;
    }

    h4 {
        margin: 5px;
    }

    h5 {
        opacity: 0.5;
        margin-top: 5px;
    }
</style>