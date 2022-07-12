<script>
    import { onDestroy, onMount } from "svelte";

    export let index = null;
    export let connection = null;
    export let icon = "";
    export let displayName = "";

    let borders = "";
    let opacity = "opacity: 0;";
    let wasSelected = false;
    let chosen = "";
    let waiting = true;
    let win = "";

    function handleSquareChoice() {
        if(!wasSelected && !waiting) {
            connection.send(`{"type": "new_move", "cell": "${index}"}`);
            chosen = "chosen";
            opacity = "opacity: 1";
            wasSelected = true;
        }
    }

    function determineBorders(index) {
        if (index == 0) borders = "top-left";
        if (index == 1 || index == 2) borders = "top";
        if (index % 3 == 0 && index <= 6 && index !== 0) borders = "left";
    }

    function handleMouseOver() {
        if(!wasSelected) {
            opacity = "opacity: 1";
        }
    }

    function handleMouseLeave() {
        if(!wasSelected) {
            opacity = "opacity: 0";
        }
    }

    function handleMessage(event) {
        const message = JSON.parse(event.data);

        switch(message.type) {
            case "new_move":
                if(message.cell == index) {
                    icon = message.icon;
                    chosen = "chosen";
                    opacity = "opacity: 1";
                    wasSelected = true;
                }
                break;
            case "starting":
                waiting = false;
                break;
            case "turn":
                if(message.player !== displayName) {
                    waiting = true;
                } else {
                    waiting = false;
                }
                break;
            case "win":
                waiting = true;
                const sequence = (message.win_sequence);
                for(let i = 0; i < sequence.length; i++) {
                    if(sequence[i] == index && sequence[i] !== " ") {
                        console.log(`${sequence[i]} ${typeof(sequence[i])}`);
                        setTimeout(() => {
                            win = "win";
                        }, (100 * i) + 100);
                    } 
                }
                break;
            case "tie":
                waiting = true;
                break;
        }
    }

    determineBorders(index);

    onMount(() => {
        connection.addEventListener("message", handleMessage);
    });

    onDestroy(() => {
        connection.removeEventListener("message", handleMessage);
    });
</script>

<svelte:head>
    <script src="https://kit.fontawesome.com/3ade200140.js" crossorigin="anonymous"></script>
</svelte:head>

<div class={`grid-square ${borders} ${chosen}`} on:click={handleSquareChoice} on:mouseover={handleMouseOver} on:focus={() => {/**/}} on:mouseleave={handleMouseLeave}>
    {#if icon == "O"}
        <i style={opacity} id={win} class="fa-solid fa-o fa-8x"></i>
    {:else}
        <i style={opacity} id={win} class="fa-solid fa-xmark fa-10x"></i>
    {/if}
</div>

<style>
    i {
        background: none;
        color: rgb(255, 255, 255);
        transition: 0.2s cubic-bezier(0, 0.55, 0.45, 1);
        opacity: 0;
    }

    #win {
        color: rgb(47, 255, 50);
    }

    .grid-square {
        cursor: pointer;
        transition: 0.2s cubic-bezier(0, 0.55, 0.45, 1);
        width: 170px;
        height: 170px;
        border-right: 1px solid rgba(255, 255, 255, 0.053);
        border-bottom: 1px solid rgba(255, 255, 255, 0.053);
        display: flex;
        justify-content: center;
        align-items: center;
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

    .chosen {
        cursor: not-allowed;
    }

    .chosen:hover {
        background-color: rgba(255, 0, 0, 0.1);
    }
</style>