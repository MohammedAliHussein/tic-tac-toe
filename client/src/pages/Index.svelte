<script>
    import { fly } from "svelte/transition";
    import { circOut } from "svelte/easing";
    import Title from "../components/IndexTitle.svelte";
    import Options from "../components/IndexOptions.svelte";

    function getIn(delayIndex) {
        return {
            delay: delayIndex * 100,
            y: 20,
            easing: circOut
        }
    }
</script>

<div class="index">
    <h1 in:fly={getIn(0)}>Tic-Tac-Toe</h1>
    <h5 in:fly={getIn(1)}>[Multiplayer]</h5>
    <Options />
</div>

<style>
    .index {
        text-align: center;
    }

    h5, h1 {
        color: white;
        outline: none;
    }
</style>