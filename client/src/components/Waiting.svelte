<script>
    import { fly } from "svelte/transition";
    import { circOut } from "svelte/easing";

    export let connected = 0;

    function getIn(delayIndex) {
        return {
            delay: delayIndex * 100,
            y: 20,
            easing: circOut
        }
    }

    function getOut(delayIndex) {
        return {
            delay: delayIndex * 100,
            y: -20,
            easing: circOut
        }
    }

</script>

<div class="waiting">
    <h3 in:fly={getIn(0)} out:fly={getOut(0)}>Waiting for Opponent.</h3>
    <h5 in:fly={getIn(1)} out:fly={getOut(1)}>[{connected}/2 Players]</h5>
    <h5 in:fly={getIn(2)} out:fly={getOut(2)}>[Who goes first is chosen randomly]</h5>
</div>

<style>
    .waiting {
        display: flex;
        justify-content: center;
        align-items: center;
        text-align: center;
        flex-direction: column;
    }

    h3, h5 {
        color: white;
    }

    h5 {
        margin-top: 1em;
    }

    h3 {
        animation: fade 2s infinite ease 0.1s;
    }

    @keyframes fade {
        0% {
            opacity: 1;
        }
        50% {
            opacity: 0.3;
        }
        100% {
            opacity: 1;
        }
    }
</style>