<script>
    import Title from "../components/IndexTitle.svelte";
    import Options from "../components/IndexOptions.svelte";
</script>

<div class="index">
    <Title text={"Tic-Tac-Toe"}/>
    <h5>[Multiplayer]</h5>
    <Options />
</div>

<style>
    .index {
        text-align: center;
    }

    h5 {
        color: white;
    }
</style>